package com.bear.brain.menu;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.bear.brain.chat.ChatFragment;
import com.bear.brain.main.StartScreen;
import com.bear.brain.ratio.RatioScreen;
import com.bear.lib.StackScreenManager;

public class Menu extends StackScreenManager {
    MainFragment mainFragment;

    public Menu(Group container) {
        super(container);
        mainFragment = new MainFragment();
        setScreen(mainFragment);
    }

    @Override
    public void backScreen() {
        super.backScreen();
    }

    public void setGame() {
        mainFragment.manager.setScreen(mainFragment.manager.getFragment(StartScreen.TAG));
    }

    public void setRating() {
        mainFragment.manager.setScreen(new RatioScreen());
    }

    public void setChat() {
        mainFragment.manager.setScreen(new ChatFragment());
    }
}
