package ray1024.games.tws;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class TiledSpace {
    private final static int EMPTY_CELL = 0;
    private final static int FULL_WATER_CELL = 100;
    private final static int ROCK = -1;
    private final static int FRAME = -2;
    private final static float maxTimer = 1.0f / 60.0f;

    private float timer = maxTimer;
    private int[][] blocks;
    private int width, height, tileSize;

    public TiledSpace(int width, int height, int tileSize) {
        if (width < 0 || height < 0) {
            width = 0;
            height = 0;
        }
        this.width = width;
        this.height = height;
        this.tileSize = tileSize;
        blocks = new int[width][height];
        for (int i = 0; i < width; ++i) {
            blocks[i][0] = FRAME;
            blocks[i][height - 1] = FRAME;
        }
        for (int i = 0; i < height; ++i) {
            blocks[0][i] = FRAME;
            blocks[height - 1][i] = FRAME;
        }
    }

    public void update() {
        int x = Gdx.input.getX() / tileSize;
        int y = (Gdx.graphics.getHeight() - Gdx.input.getY() - 1) / tileSize;
        if (!(x < 1 || y < 1 || x > width - 2 || y > height - 2)) {
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT))
                blocks[x][y] = FULL_WATER_CELL;
            if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT))
                blocks[x][y] = ROCK;
        }
        updateWater();
    }

    private void updateWater() {
        for (int i = 1; i < width - 1; ++i) {
            for (int j = 1; j < height - 1; ++j) {
                if (blocks[i][j] < EMPTY_CELL || blocks[i][j] > FULL_WATER_CELL) continue;
                int down = Math.min(blocks[i][j], howMuchAddTo(i, j - 1));
                if (down > 0) {
                    blocks[i][j - 1] += down;
                    blocks[i][j] -= down;
                }
                int left = Math.min(blocks[i][j], howMuchAddTo(i - 1, j));
                int right = Math.min(blocks[i][j], howMuchAddTo(i + 1, j));
                int avg, remains;
                if (left != 0 && right != 0) {
                    avg = (blocks[i - 1][j] + blocks[i][j] + blocks[i + 1][j]) / 3;
                    remains = (blocks[i - 1][j] + blocks[i][j] + blocks[i + 1][j]) % 3;
                    blocks[i - 1][j] = avg;
                    blocks[i][j] = avg + remains;
                    blocks[i + 1][j] = avg;
                } else if (left == 0 && right != 0) {
                    avg = (blocks[i][j] + blocks[i + 1][j]) >> 1;
                    remains = (blocks[i][j] + blocks[i + 1][j]) & 1;
                    blocks[i][j] = avg + remains;
                    blocks[i + 1][j] = avg;
                } else if (left != 0) {
                    avg = (blocks[i][j] + blocks[i - 1][j]) >> 1;
                    remains = (blocks[i][j] + blocks[i - 1][j]) & 1;
                    blocks[i][j] = avg + remains;
                    blocks[i - 1][j] = avg;
                }
            }
        }
    }

    private boolean canAddTo(int i, int j) {
        return blocks[i][j] >= EMPTY_CELL && blocks[i][j] < FULL_WATER_CELL;
    }

    private int howMuchAddTo(int i, int j) {
        return canAddTo(i, j) ? FULL_WATER_CELL - blocks[i][j] : 0;
    }


    public void draw(ShapeRenderer renderer) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0, x = 0; i < width; ++i, x += tileSize) {
            for (int j = 0, y = 0; j < height; ++j, y += tileSize) {
                if (blocks[i][j] == FRAME) {
                    renderer.setColor(Color.YELLOW);
                    renderer.rect(x, y, tileSize, tileSize);
                } else if (blocks[i][j] == EMPTY_CELL) {
                } else if (blocks[i][j] == FULL_WATER_CELL) {
                    renderer.setColor(Color.BLUE);
                    renderer.rect(x, y, tileSize, tileSize);
                } else if (blocks[i][j] == ROCK) {
                    renderer.setColor(Color.GRAY);
                    renderer.rect(x, y, tileSize, tileSize);
                } else {
                    if (!canAddTo(i, j - 1)) {
                        renderer.setColor(Color.BLUE);
                        renderer.rect(x, y, tileSize, (tileSize * blocks[i][j]) / FULL_WATER_CELL);
                    } else {
                        renderer.setColor(0.0f, 0.0f, 1.0f, ((float) blocks[i][j]) / ((float) FULL_WATER_CELL));
                        renderer.rect(x, y, tileSize, tileSize);
                    }
                }
            }
        }
        renderer.end();
    }
}
