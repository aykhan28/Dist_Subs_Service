import socket
import struct
from Capacity_pb2 import Capacity
import matplotlib.pyplot as plt
from collections import deque
import threading
import traceback
import time

# Veri güncellemelerinde senkronizasyon için kilit
data_mutex = threading.Lock()
program_start_time = time.time()  # Programın başlatıldığı zaman

# Her sunucu için kapasite verilerini saklayan kuyruklar
server_capacity_buffers = {
    'server1': deque(maxlen=200),
    'server2': deque(maxlen=200),
    'server3': deque(maxlen=200)
}

# Kapasite verilerini grafik üzerinde gösteren fonksiyon
def render_capacity_graph():
    plt.ion()
    fig, ax = plt.subplots(figsize=(14, 8))

    # Sunucular için renk ve çizgi stilleri
    server_styles = {
        'server1': {'color': '#1f77b4', 'linestyle': '-', 'label': 'Server 1'},
        'server2': {'color': '#ff7f0e', 'linestyle': '--', 'label': 'Server 2'},
        'server3': {'color': '#2ca02c', 'linestyle': '-.', 'label': 'Server 3'}
    }

    server_plot_lines = {}

    ax.set_title("Sunucuların Kapasite Durumları")
    ax.set_xlabel("Zaman (saniye)")
    ax.set_ylabel("Kapasite")
    ax.set_ylim(0, 120)
    ax.grid(True, linestyle='--', alpha=0.5)

    plt.tight_layout()

    while True:
        with data_mutex:
            # Veri içeren sunucuları belirle
            active_servers = [server for server, data in server_capacity_buffers.items() if data]

            # Her sunucu için grafik çizgilerini güncelle
            for server_name in active_servers:
                if server_name not in server_plot_lines:
                    style = server_styles[server_name]
                    server_plot_lines[server_name] = ax.plot([], [], color=style['color'], linestyle=style['linestyle'], label=style['label'])[0]
                
                data_points = server_capacity_buffers[server_name]
                timestamps = [point[1] for point in data_points]
                capacity_values = [point[0] for point in data_points]
                
                server_plot_lines[server_name].set_data(timestamps, capacity_values)
            
            # X eksenini güncelle
            if active_servers:
                max_time = max(max(point[1] for point in server_capacity_buffers[server]) for server in active_servers)
                ax.set_xlim(0, max_time + 5)
                ax.legend()
        
        ax.relim()
        ax.autoscale_view(scaley=True, scalex=False)
        plt.pause(5)
        

# İstemciden gelen kapasite verilerini işleyen fonksiyon
def handle_capacity_data(client_socket):
    while True:
        try:
            # Gelen verinin uzunluğunu al
            data_size = client_socket.recv(4)
            if not data_size:
                break
            payload_length = struct.unpack("!I", data_size)[0]
            payload_data = client_socket.recv(payload_length)
            capacity_record = Capacity()
            
            # Gelen veriyi ayrıştır
            try:
                capacity_record.ParseFromString(payload_data)
            except Exception as parse_error:
                print(f"Veri çözümleme hatası: {parse_error}")
                print(traceback.format_exc())
                continue
            
            print(f"Kapasite Alındı - Sunucu: Server{capacity_record.server_id}, Durum: {capacity_record.serverX_status}, Zaman: {capacity_record.timestamp}")
            
            # Veriyi ilgili sunucunun kuyruğuna ekle
            with data_mutex:
                elapsed_time = time.time() - program_start_time
                target_server = f"server{capacity_record.server_id}"
                if target_server in server_capacity_buffers:
                    server_capacity_buffers[target_server].append((capacity_record.serverX_status, elapsed_time))
                else:
                    print(f"Bilinmeyen sunucu ID: {capacity_record.server_id}")
        
        except Exception as process_error:
            print(f"Veri işleme sırasında hata: {process_error}")
            print(traceback.format_exc())
            break

# Sunucuyu başlatan fonksiyon
def start_capacity_server():
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as capacity_server_socket:
        capacity_server_socket.bind(('localhost', 6000))
        capacity_server_socket.listen(1)

        while True:
            client_conn, client_addr = capacity_server_socket.accept()
            print(f"İstemci bağlandı: {client_addr}")
            handle_capacity_data(client_conn)

if __name__ == "__main__":
    server_thread = threading.Thread(target=start_capacity_server, daemon=True)
    server_thread.start()
    render_capacity_graph()
