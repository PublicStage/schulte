package com.bear.brain.menu;

import com.bear.brain.main.StartScreen;
import com.bear.lib.BaseScreenManager;
import com.bear.lib.Fragment;

public class MainFragment extends Fragment {
    BaseScreenManager manager;

    public MainFragment() {
        manager = new BaseScreenManager(this);
        manager.setScreen(new StartScreen(), StartScreen.TAG);
    }

    @Override
    protected void sizeChanged() {
        manager.resize((int) getWidth(), (int) getHeight());
    }

    @Override
    public boolean back() {
        return manager.currentFragmentBack();
    }

    @Override
    public void dispose() {
        manager.dispose();
    }
}
