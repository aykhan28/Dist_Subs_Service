import socket
from Capacity_pb2 import Capacity
import matplotlib.pyplot as plt
import time

HOST = 'localhost'
PORT = 6000

subscriber_counts = {"Server1": 0, "Server2": 0, "Server3": 0}
last_update_times = {"Server1": time.time(), "Server2": time.time(), "Server3": time.time()}
DISCONNECT_THRESHOLD = 5

def plot_subscriber_counts():
    plt.clf()
    servers = list(subscriber_counts.keys())
    values = list(subscriber_counts.values())

    plt.bar(servers, values, color=['blue', 'green', 'red'], edgecolor='black')
    plt.ylabel('Subscriber Count (Normalized)', fontsize=14)
    plt.title('Subscriber Counts by Server', fontsize=16)
    plt.ylim(0, 5)
    plt.xticks(fontsize=12)
    plt.yticks(fontsize=12)
    plt.tight_layout()
    plt.draw()
    plt.pause(0.01)

def reset_stale_servers():
    current_time = time.time()
    for server, last_update in last_update_times.items():
        if current_time - last_update > DISCONNECT_THRESHOLD:
            subscriber_counts[server] = 0

def start_server():
    plt.ion()
    plt.figure()
    plt.show()

    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as server_socket:
        server_socket.bind((HOST, PORT))
        server_socket.listen(1)
        print(f"Python server listening on {HOST}:{PORT}")

        while True:
            reset_stale_servers()
            plot_subscriber_counts()

            try:
                server_socket.settimeout(1)
                conn, addr = server_socket.accept()
                with conn:
                    print(f"Connected by {addr}")
                    data = b""
                    while True:
                        chunk = conn.recv(1024)
                        if not chunk:
                            break
                        data += chunk

                    if data:
                        capacity = Capacity()
                        try:
                            capacity.ParseFromString(data)
                            print(f"Parsed capacity: {capacity.subscriber_count}")

                            server_name = f"Server{capacity.server_port % 4000}"

                            if server_name in subscriber_counts:
                                subscriber_counts[server_name] = capacity.subscriber_count
                                last_update_times[server_name] = time.time()
                                print(f"{server_name}: {capacity.subscriber_count} subscribers")

                        except Exception as e:
                            print(f"Error parsing data: {e}")
            except socket.timeout:
                continue

start_server()
