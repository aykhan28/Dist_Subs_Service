import socket
import threading
from concurrent.futures import ThreadPoolExecutor
import matplotlib.pyplot as plt
from collections import defaultdict
from Capacity_pb2 import Capacity

HOST = 'localhost'
PORT = 6000

# Kapasite verilerini saklamak için bir sözlük
capacity_data = defaultdict(list)
lock = threading.Lock()

def handle_client_connection(conn, addr):
    print(f"Yeni bağlantı: {addr}")
    try:
        while True:
            # Kapasite verilerini al
            data = conn.recv(1024)
            if not data:
                break

            capacity_message = Capacity()
            capacity_message.ParseFromString(data)

            # Kapasite bilgilerini işleyip sakla
            server_id = capacity_message.serverXStatus
            capacity_value = capacity_message.timestamp

            with lock:
                capacity_data[server_id].append(capacity_value)
                if len(capacity_data[server_id]) > 20:
                    capacity_data[server_id] = capacity_data[server_id][-20:]

            print(f"Server {server_id}: Kapasite Zaman Damgası {capacity_value}")

    except Exception as e:
        print(f"Bağlantı hatası ({addr}): {e}")
    finally:
        conn.close()

def update_plot():
    plt.ion()
    fig, ax = plt.subplots()
    while True:
        with lock:
            ax.clear()
            for server_id, values in capacity_data.items():
                ax.plot(range(len(values)), values, label=f"Server {server_id}")

        ax.set_xlabel("Zaman (5 saniye aralıklarla)")
        ax.set_ylabel("Kapasite Değerleri")
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

if __name__ == "__main__":
    threading.Thread(target=start_server, daemon=True).start()
    update_plot()
