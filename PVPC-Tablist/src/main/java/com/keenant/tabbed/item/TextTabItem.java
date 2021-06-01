package com.keenant.tabbed.item;

import com.keenant.tabbed.util.Skin;
import com.keenant.tabbed.util.Skins;

import java.util.Objects;

/**
 * A tab item with custom text, ping and skin.
 */
public class TextTabItem implements TabItem {
    private String text;
    private int ping;
    private Skin skin;

    private String newText;
    private int newPing;
    private Skin newSkin;

    @Override
    public String getText() {
        return text;
    }

    @Override
    public int getPing() {
        return ping;
    }

    @Override
    public Skin getSkin() {
        return skin;
    }

    public String getNewText() {
        return newText;
    }

    public int getNewPing() {
        return newPing;
    }

    public Skin getNewSkin() {
        return newSkin;
    }

    public TextTabItem(String text) {
        this(text, 1000);
    }

    public TextTabItem(String text, int ping) {
        this(text, ping, Skins.DEFAULT_SKIN);
    }

    public TextTabItem(String text, int ping, Skin skin) {
        this.newText = text;
        this.newPing = ping;
        this.newSkin = skin;
        updateText();
        updatePing();
        updateSkin();
    }

    public void setText(String text) {
        this.newText = text;
    }

    public void setPing(int ping) {
        this.newPing = ping;
    }

    public void setSkin(Skin skin) {
        this.newSkin = skin;
    }

    @Override
    public boolean updateText() {
        boolean update = !Objects.equals(this.text, this.newText);
        this.text = this.newText;
        return update;
    }

    @Override
    public boolean updatePing() {
        boolean update = this.ping != this.newPing;
        this.ping = this.newPing;
        return update;
    }

    @Override
    public boolean updateSkin() {
        boolean update = !Objects.equals(this.skin, this.newSkin);
        this.skin = this.newSkin;
        return update;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TextTabItem))
            return false;
        TextTabItem other = (TextTabItem) object;
        return this.text.equals(other.getText()) && this.skin.equals(other.getSkin()) && this.ping == other.getPing();
    }
}
