import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class Salon extends BaseEntity implements IKayit {
    private Map<Film, List<String>> filmVeSaatler = new HashMap<>();
    private List<Musteri> musteriler = new ArrayList<>();
    private static final int MAX_KAPASITE = 50;

    public Salon(int id, String name) {
        super(id, name);
    }

    // Film eklemek ve saatlerini belirlemek
    public void filmEkle(Film film, List<String> saatler) {
        filmVeSaatler.put(film, saatler);
    }

    public Map<Film, List<String>> getFilmVeSaatler() {
        return filmVeSaatler;
    }

    public boolean bosYerVarMi() {
        return musteriler.size() < MAX_KAPASITE;
    }

    public void musteriEkle(Musteri musteri) {
        if (bosYerVarMi()) {
            musteriler.add(musteri);
            System.out.println(musteri.getAd() + " başarıyla " + getName() + " salonuna kaydedildi.");
        } else {
            System.out.println(getName() + " salonu dolu! Yeni müşteri eklenemiyor.");
        }
    }

    @Override
    public void bilgiGoster() {
        System.out.println("         ===== Salonlar ve Gösterim Bilgileri =====");
        System.out.println("----------------------------------------------------------");

        // Salonları 2 satır 3 sütun şeklinde düzenlemek için
        int rowCount = 2; // 2 satır
        int columnCount = 3; // 3 sütun

        // Burada, salonların ve film bilgilerini dışarıdan alacağız
        // SinemaUygulamasi'ndaki salonları listeleyeceğiz
        List<Salon> salonlar = SinemaUygulamasi.getSalonlar();

        int salonIndex = 0;
        for (int i = 0; i < rowCount; i++) {
            // Her satırda 3 salon olacak şekilde döngü kuruyoruz
            for (int j = 0; j < columnCount; j++) {
                if (salonIndex < salonlar.size()) {
                    Salon salon = salonlar.get(salonIndex);
                    salonIndex++;

                    // Salon bilgilerini yazdır
                    System.out.printf("| %-10s | %-20s | %-15s |\n",
                            "Salon ID: " + salon.getId(),
                            "Salon: " + salon.getName(),
                            "Boş Koltuk: " + (MAX_KAPASITE - salon.getMusteriler().size()));

                    // Gösterimdeki film ve saatleri yazdır
                    if (!salon.getFilmVeSaatler().isEmpty()) {
                        for (Map.Entry<Film, List<String>> entry : salon.getFilmVeSaatler().entrySet()) {
                            System.out.printf("   Film: %-20s Saatler: %-20s\n",
                                    entry.getKey().getName(),
                                    entry.getValue());
                        }
                    } else {
                        System.out.println("   Gösterimde film yok.");
                    }

                    System.out.println("----------------------------------------------------------");
                }
            }

            // Salonlar bir satırda bittiğinde araya bir boş satır ekleyelim
            System.out.println();
        }
    }

    @Override
    public void kayitOl(Musteri musteri) {
        musteriEkle(musteri);
    }

    public List<Musteri> getMusteriler() {
        return musteriler;
    }


    // Filmleri JSON dosyasına kaydetme fonksiyonu
    public void kaydetFilmlerJson() {
        JsonArray filmArray = new JsonArray();

        for (Salon salon : SinemaUygulamasi.getSalonlar()) {
            for (Map.Entry<Film, List<String>> entry : salon.getFilmVeSaatler().entrySet()) {
                JsonObject filmObject = new JsonObject();
                Film film = entry.getKey();
                List<String> saatler = entry.getValue();

                filmObject.addProperty("name", film.getName());
                filmObject.addProperty("duration", film.getDuration());
                filmObject.addProperty("genre", film.getGenre());

                JsonArray salonlarArray = new JsonArray();
                salonlarArray.add(salon.getName());

                filmObject.add("salonlar", salonlarArray);

                // Saatler listesine eleman eklemek için yeni bir JsonArray oluşturuyoruz
                JsonArray saatlerArray = new JsonArray();
                for (String saat : saatler) {
                    saatlerArray.add(saat);  // Her bir saat elemanını saatler array'ine ekliyoruz
                }

                filmObject.add("saatler", saatlerArray);  // Saatler array'ini filmObject'e ekliyoruz

                filmArray.add(filmObject);
            }
        }

        // Filmleri "Filmler.json" dosyasına kaydediyoruz
        try (FileWriter writer = new FileWriter("Filmler.json")) {
            writer.write(new Gson().toJson(filmArray));
            System.out.println("Filmler başarıyla Filmler.json dosyasına kaydedildi.");
        } catch (IOException e) {
            e.printStackTrace();  // Hata mesajlarını görmek için
        }
    }

    // Filmleri JSON dosyasından okuma fonksiyonu
    public void okuFilmlerJson() {
        try {
            JsonReader reader = new JsonReader(new FileReader("Filmler.json"));
            JsonArray filmArray = JsonParser.parseReader(reader).getAsJsonArray();

            for (JsonElement filmElement : filmArray) {
                JsonObject filmObject = filmElement.getAsJsonObject();
                String name = filmObject.get("name").getAsString();
                int duration = filmObject.get("duration").getAsInt();
                String genre = filmObject.get("genre").getAsString();

                Film film = new Film(name, duration, genre);
                JsonArray salonlarArray = filmObject.getAsJsonArray("salonlar");
                for (JsonElement salonElement : salonlarArray) {
                    String salonName = salonElement.getAsString();
                    Salon salon = new Salon(0, salonName);  // Salon ID'yi almadık, çünkü dosyada yer almıyor
                    salon.filmEkle(film, new ArrayList<>());  // Film saatlerini ekleyebilirsiniz burada
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
