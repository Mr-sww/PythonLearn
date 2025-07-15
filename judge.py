import sys
import subprocess
import json
import os

def run_user_code(code, input_str, time_limit=2, memory_limit=128*1024*1024, max_output=4096):
    code_file = 'user_code.py'
    with open(code_file, 'w', encoding='utf-8') as f:
        f.write(code)
    def set_limits():
        try:
            import resource
            resource.setrlimit(resource.RLIMIT_AS, (memory_limit, memory_limit))
        except ImportError:
            pass  # Windows 下无 resource，跳过
    try:
        result = subprocess.run(
            [sys.executable, code_file],
            input=input_str.encode(),
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            timeout=time_limit,
            preexec_fn=set_limits if os.name != 'nt' else None  # 仅非 Windows 下设置
        )
        output = result.stdout.decode('utf-8', errors='replace')[:max_output]
        stderr = result.stderr.decode('utf-8', errors='replace')[:max_output]
        return {
            'output': output,
            'stderr': stderr,
            'returncode': result.returncode
        }
    except subprocess.TimeoutExpired:
        return {'output': '', 'stderr': 'Time Limit Exceeded', 'returncode': -1}
    finally:
        try:
            os.remove(code_file)
        except Exception:
            pass

def compare_output(actual, expected):
    return actual.strip() == expected.strip()

if __name__ == '__main__':
    try:
        raw = sys.stdin.read()
        with open('judge_debug.log', 'w', encoding='utf-8') as f:
            f.write('收到输入：' + repr(raw))
        data = json.loads(raw)
        code = data.get('code', '')
        test_cases = data.get('testCases')
        if test_cases:
            results = []
            for case in test_cases:
                input_str = case.get('input', '')
                expected = case.get('expected', '')
                result = run_user_code(code, input_str)
                actual = result['output']
                result['passed'] = compare_output(actual, expected)
                result['input'] = input_str
                result['expected'] = expected
                result['actual'] = actual
                results.append(result)
            print(json.dumps(results, ensure_ascii=False))
        else:
            input_str = data.get('input', '')
            result = run_user_code(code, input_str)
            print(json.dumps(result, ensure_ascii=False))
    except Exception as e:
        with open('judge_debug.log', 'a', encoding='utf-8') as f:
            f.write('\n异常: ' + repr(e))
        print(json.dumps({'output': '', 'stderr': str(e), 'returncode': -1})) 