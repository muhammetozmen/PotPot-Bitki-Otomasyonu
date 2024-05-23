package application;

public class Plant {
    String name;
    int minTemp;
    int maxTemp;
    double moisture;
    String isPoisonous;
    String image;
    
    public Plant(String name, int minTemp, int maxTemp, double moisture, String isPoisonous, String image) {
        this.name = name;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.moisture = moisture;
        this.isPoisonous = isPoisonous;
        this.image= image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(int minTemp) {
        this.minTemp = minTemp;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(int maxTemp) {
        this.maxTemp = maxTemp;
    }

    public double getMoisture() {
        return moisture;
    }

    public void setMoisture(double moisture) {
        this.moisture = moisture;
    }

    public String getIsPoisonous() {
        return isPoisonous;
    }

    public void setIsPoisonous(String isPoisonous) {
        this.isPoisonous = isPoisonous;
    }
    
    public String getimage() {
        return image;
    }

    public void setimage(String image) {
        this.image = image;
    }
}
