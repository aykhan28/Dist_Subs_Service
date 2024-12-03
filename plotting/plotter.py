import socket
import threading
from concurrent.futures import ThreadPoolExecutor
import matplotlib.pyplot as plt
from collections import defaultdict
from Capacity_pb2 import Capacity

HOST = 'localhost'
PORT = 6000

capacity_data = defaultdict(list)

def handle_client_connection(conn, addr):
    print(f"Yeni bağlantı: {addr}")
    try:
        while True:
            data = conn.recv(1024)
            if not data:
                break

            capacity_message = Capacity()
            capacity_message.ParseFromString(data)

            server_id = capacity_message.serverXStatus
            capacity_value = capacity_message.timestamp
            capacity_data[server_id].append(capacity_value)

            if len(capacity_data[server_id]) > 20:
                capacity_data[server_id] = capacity_data[server_id][-20:]

    except Exception as e:
        print(f"Bağlantı hatası: {e}")
    finally:
        conn.close()

def update_plot():
    plt.ion()
    fig, ax = plt.subplots()
    while True:
        ax.clear()
        for server_id, values in capacity_data.items():
            ax.plot(range(len(values)), values, label=f"Server {server_id}")
        ax.set_xlabel("Zaman")
        ax.set_ylabel("Kapasite")
        ax.set_title("Sunucu Kapasite Grafiği")
        ax.legend()
        plt.pause(5)

def start_server():
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as server_socket:
        server_socket.bind((HOST, PORT))
        server_socket.listen(5)
        print(f"Plotter Sunucusu {HOST}:{PORT} üzerinde çalışıyor...")

        with ThreadPoolExecutor() as executor:
            while True:
                conn, addr = server_socket.accept()
                executor.submit(handle_client_connection, conn, addr)

threading.Thread(target=start_server, daemon=True).start()
update_plot()
