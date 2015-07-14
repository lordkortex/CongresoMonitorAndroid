package monitor.congreso.com.hn.dto;

/**
 * Created by CortesMoncada on 08/02/2015.
 */
import android.graphics.Bitmap;

/**
 *
 * @author manish.s
 *
 */

public class MenuItem {
    Bitmap image;
    String title;

    public MenuItem(Bitmap image, String title) {
        super();
        this.image = image;
        this.title = title;
    }
    public Bitmap getImage() {
        return image;
    }
    public void setImage(Bitmap image) {
        this.image = image;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }


}
