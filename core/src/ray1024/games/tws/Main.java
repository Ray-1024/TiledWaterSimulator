package ray1024.games.tws;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends ApplicationAdapter {
    private ShapeRenderer shapeRenderer;
    private TiledSpace tiledSpace;

    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        tiledSpace = new TiledSpace(100, 100, 8);
    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.BLACK);
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
        tiledSpace.update();
        tiledSpace.draw(shapeRenderer);
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
