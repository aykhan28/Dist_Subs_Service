# Dağıtık Abonelik Sistemi (Distributed Subscriber Service)
Derleme ve Çalıştırma aşamaları:
1. dist_servers klasöründe ***protoc --java_out=. <Proto Dosya İsmi>.proto*** , panel klasöründe ***protoc --ruby_out=. <Proto Dosya İsmi>.proto*** ve clients klasöründe ***protoc --java_out=. <Proto Dosya İsmi>.proto*** komutunu her bir protobuf dosyası için yazarak protoları derliyoruz.
2. dist_servers klasöründe: ***javac -cp ".;com/google/protobuf/protobuf-java-4.28.3.jar" \*.java***
3. Java dosyalarını çalıştırmak için: ***java -cp ".;com/google/protobuf/protobuf-java-4.28.3.jar" <Server adı>***
4. Clients klasöründe;
***javac -cp ".;com/google/protobuf/protobuf-java-4.28.3.jar" *.java***
     Java dosyalarını çalıştırmak için; ***java -cp ".;com/google/protobuf/protobuf-java-4.28.3.jar" <Client adı>*** (client'ı çalıştırırken Java server'ları çalışır durumda olmalıdır!) 


### plotter.py Solar System Exploration, 1950s – 1960s

- [ ] Mercury
- [x] Venus
- [x] Earth (Orbit/Moon)
- [x] Mars
- [ ] Jupiter
- [ ] Saturn
- [ ] Uranus
- [ ] Neptune
- [ ] Comet Haley

### admin.rb Solar System Exploration, 1950s – 1960s

- [ ] Mercury
- [x] Venus
- [x] Earth (Orbit/Moon)
- [x] Mars
- [ ] Jupiter
- [ ] Saturn
- [ ] Uranus
- [ ] Neptune
- [ ] Comet Haley

### ServerX.java Solar System Exploration, 1950s – 1960s

- [ ] Mercury
- [x] Venus
- [x] Earth (Orbit/Moon)
- [x] Mars
- [ ] Jupiter
- [ ] Saturn
- [ ] Uranus
- [ ] Neptune
- [ ] Comet Haley
