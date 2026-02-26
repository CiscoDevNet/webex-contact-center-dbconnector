import requests
import time
import statistics

# Configuration
url = "/api/query/dev/mytesttable=param1=1"

num_requests = 100

print(f"Starting benchmark for {url}")
print(f"Performing {num_requests} requests...")

response_times = []
success_count = 0
error_count = 0

start_total_time = time.time()

for i in range(num_requests):
    try:
        start_req_time = time.time()
        response = requests.get(url)
        end_req_time = time.time()
        
        duration_ms = (end_req_time - start_req_time) * 1000
        response_times.append(duration_ms)
        
        if response.status_code == 200:
            success_count += 1
            # Optional: print a dot for progress
            print(".", end="", flush=True)
        else:
            error_count += 1
            print("x", end="", flush=True)
            # print(f"\nRequest {i+1} failed: {response.status_code}")
            
    except Exception as e:
        error_count += 1
        print("E", end="", flush=True)
        if i == 0:
             print(f"\nRequest {i+1} exception: {e}")

end_total_time = time.time()
total_duration_seconds = end_total_time - start_total_time

print("\n\n--- Benchmark Results ---")
print(f"Total Duration: {total_duration_seconds:.2f} seconds")
print(f"Requests: {num_requests}")
print(f"Success: {success_count}")
print(f"Failed: {error_count}")

if response_times:
    print(f"Average Latency: {statistics.mean(response_times):.2f} ms")
    print(f"Median Latency: {statistics.median(response_times):.2f} ms")
    print(f"Min Latency: {min(response_times):.2f} ms")
    print(f"Max Latency: {max(response_times):.2f} ms")
