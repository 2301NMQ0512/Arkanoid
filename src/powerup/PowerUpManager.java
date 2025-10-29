package powerup;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class PowerUpManager {
    private final List<PowerUp> list = new ArrayList<PowerUp>();
    private final Random rand = new Random();
    private final int panelHeight;

    public PowerUpManager(int panelHeight) {
        this.panelHeight = panelHeight;
    }

    public void spawnRandomAt(int x, int y) {
        if (rand.nextDouble() > 0.25) return;
        PowerUpType[] types = PowerUpType.values();
        PowerUpType t = types[rand.nextInt(types.length)];
        list.add(new PowerUp(x, y, t));
    }

    public void update() {
        Iterator<PowerUp> it = list.iterator();
        while (it.hasNext()) {
            PowerUp p = it.next();
            if (!p.isActive()) { it.remove(); continue; }
            p.update();
            if (p.getY() > panelHeight + 50) it.remove();
        }
    }

    public void draw(Graphics2D g) {
        for (PowerUp p : list) p.draw(g);
    }

    public List<PowerUp> getList() { return list; }
}
