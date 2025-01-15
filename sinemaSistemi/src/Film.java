public class Film {
    private String name;
    private int duration;
    private String genre;

    public Film(String name, int duration, String genre) {
        this.name = name;
        this.duration = duration;
        this.genre = genre;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public String getGenre() {
        return genre;
    }

    public void bilgiGoster() {
        System.out.println("Film: " + name + " (" + genre + ", " + duration + " dakika)");
    }
}
