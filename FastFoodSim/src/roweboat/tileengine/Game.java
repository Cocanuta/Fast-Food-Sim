/*
 * Copyright 2014 Cocanuta.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package roweboat.tileengine;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

/**
 *
 * @author Cocanuta
 */
public class Game implements ApplicationListener, InputProcessor {
    
    	Texture texture_floor;
        Texture texture_floor2;
        Texture texture_floor3;
	OrthographicCamera cam;
	SpriteBatch batch;	
	final Sprite[][] sprites = new Sprite[10][10];
	final Matrix4 matrix = new Matrix4();
        
        byte[][] map = {
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
			{ 1, 0, 1, 1, 1, 1, 1, 0, 1, 1 },
			{ 1, 0, 1, 1, 1, 1, 1, 0, 1, 0 },
			{ 1, 0, 1, 1, 1, 0, 1, 1, 1, 0 },
			{ 1, 2, 1, 1, 0, 0, 1, 1, 1, 0 },
                        { 1, 2, 1, 1, 0, 1, 1, 1, 1, 0 },
                        { 1, 0, 1, 1, 1, 1, 1, 2, 1, 0 },
                        { 1, 0, 1, 1, 1, 1, 1, 1, 1, 1 },
                        { 1, 0, 1, 0, 1, 1, 1, 1, 1, 1 },
                        { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }
		};
        public static float clamp(float val, float min, float max) {
            return Math.max(min, Math.min(max, val));
        }
        
	@Override public void create() {
		texture_floor = new Texture(Gdx.files.internal("tile.png"));
                texture_floor2 = new Texture(Gdx.files.internal("tile2.png"));
                texture_floor3 = new Texture(Gdx.files.internal("tile3.png"));
		cam = new OrthographicCamera(10, 10 * (Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth()));			
		cam.position.set(5, 5, 10);
		cam.direction.set(-1, -1, -1);
		cam.near = 1;
		cam.far = 100;		
		matrix.setToRotation(new Vector3(1, 0, 0), 90);
                
                
 
		for(int z = 0; z < 10; z++) {
			for(int x = 0; x < 10; x++) {
                            int y = z;
                            if(map[x][y] == 1) {
				sprites[x][z] = new Sprite(texture_floor);
				sprites[x][z].setPosition(x,z);
				sprites[x][z].setSize(1, 1);
                            }
                            else if(map[x][y] == 0) {
                                sprites[x][z] = new Sprite(texture_floor2);
				sprites[x][z].setPosition(x,z);
				sprites[x][z].setSize(1, 1);
                            }
                            else if(map[x][y] == 2) {
                                sprites[x][z] = new Sprite(texture_floor3);
				sprites[x][z].setPosition(x,z);
				sprites[x][z].setSize(1, 1);
                            }
                            }
                            }
		
                
 
		batch = new SpriteBatch();
 
		Gdx.input.setInputProcessor(this);
        }

	@Override public void render() {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		cam.update();		
 
		batch.setProjectionMatrix(cam.combined);
		batch.setTransformMatrix(matrix);
		batch.begin();
		for(int z = 0; z < 10; z++) {
			for(int x = 0; x < 10; x++) {
                            int y = z;
				sprites[x][z].draw(batch);
                            }
		}
		batch.end();
 
		checkTileTouched();
	}

	public void resize (int width, int height) {
	}

	public void pause () {
	}

	public void resume () {
	}

	public void dispose () {
	}
        
final Plane xzPlane = new Plane(new Vector3(0, 1, 0), 0);
	final Vector3 intersection = new Vector3();
	Sprite lastSelectedTile = null;
 
	private void checkTileTouched() {
	
	}


    	final Vector3 curr = new Vector3();
	final Vector3 last = new Vector3(-1, -1, -1);
	final Vector3 delta = new Vector3();
	@Override public boolean touchDragged (int x, int y, int pointer) {
		Ray pickRay = cam.getPickRay(x, y);
		Intersector.intersectRayPlane(pickRay, xzPlane, curr);
 
		if(!(last.x == -1 && last.y == -1 && last.z == -1)) {
			pickRay = cam.getPickRay(last.x, last.y);
			Intersector.intersectRayPlane(pickRay, xzPlane, delta);			
			delta.sub(curr);
			cam.position.add(delta.x, delta.y, delta.z);
		}
		last.set(x, y, 0);
		return false;
	}
 
	@Override public boolean touchUp(int x, int y, int pointer, int button) {
		last.set(-1, -1, -1);
		return false;
	}

    @Override
    public boolean keyDown(int keycode) {
                return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
                return true;
            
    }

}
