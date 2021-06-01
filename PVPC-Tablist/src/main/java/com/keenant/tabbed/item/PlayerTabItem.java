package com.keenant.tabbed.item;

import com.keenant.tabbed.util.Reflection;
import com.keenant.tabbed.util.Skin;
import com.keenant.tabbed.util.Skins;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * A tab item that represents a player.
 */
public class PlayerTabItem implements TabItem {

    private final Player player;
    private final PlayerProvider<String> textProvider;
    private final PlayerProvider<Skin> skinProvider;
    private String text;
    private int ping;
    private Skin skin;

    public Player getPlayer() {
        return player;
    }

    public PlayerProvider<String> getTextProvider() {
        return textProvider;
    }

    public PlayerProvider<Skin> getSkinProvider() {
        return skinProvider;
    }

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

    public static PlayerProvider<String> getNameProvider() {
        return NAME_PROVIDER;
    }

    public static PlayerProvider<String> getDisplayNameProvider() {
        return DISPLAY_NAME_PROVIDER;
    }

    public static PlayerProvider<String> getListNameProvider() {
        return LIST_NAME_PROVIDER;
    }

    public PlayerTabItem(Player player, PlayerProvider<String> textProvider, PlayerProvider<Skin> skinProvider) {
        this.player = player;
        this.textProvider = textProvider;
        this.skinProvider = skinProvider;
        this.text = textProvider.get(player);
        this.ping = getNewPing();
        this.skin = skinProvider.get(player);
        updateText();
        updatePing();
        updateSkin();
    }

    public PlayerTabItem(Player player, PlayerProvider<String> textProvider) {
        this(player, textProvider, SKIN_PROVIDER);
    }

    public PlayerTabItem(Player player) {
        this(player, LIST_NAME_PROVIDER);
    }

    @Override
    public boolean updateText() {
        if (!this.player.isOnline() || !this.player.isValid())
            return false;

        String newText = this.textProvider.get(this.player);
        boolean update = this.text == null || !newText.equals(this.text);
        this.text = newText;
        return update;
    }

    @Override
    public boolean updatePing() {
        if (!this.player.isOnline() || !this.player.isValid())
            return false;

        int newPing = getNewPing();
        boolean update = newPing != ping;
        this.ping = newPing;
        return update;
    }

    @Override
    public boolean updateSkin() {
        if (!this.player.isOnline() || !this.player.isValid())
            return false;

        Skin newSkin = this.skinProvider.get(this.player);
        boolean update = this.skin == null || !newSkin.equals(this.skin);
        this.skin = newSkin;
        return update;
    }

    private int getNewPing() { //Change to other version
        return ((CraftPlayer) this.player).getHandle().ping;
        /*try {
            Object craftPlayer = Reflection.getHandle(this.player);
            return craftPlayer.getClass().getDeclaredField("ping").getInt(craftPlayer);
        } catch (Exception e) {
            throw new RuntimeException("couldn't get player ping", e);
        }*/
    }

    private static PlayerProvider<String> NAME_PROVIDER = new PlayerProvider<String>() {

        @Override
        public String get(Player player) {
            return player.getName();
        }
    };

    private static PlayerProvider<String> DISPLAY_NAME_PROVIDER = new PlayerProvider<String>() {
        @Override
        public String get(Player player) {
            return player.getDisplayName();
        }
    };

    private static PlayerProvider<String> LIST_NAME_PROVIDER = new PlayerProvider<String>() {
        @Override
        public String get(Player player) {
            return player.getPlayerListName();
        }
    };

    private static PlayerProvider<Skin> SKIN_PROVIDER = new PlayerProvider<Skin>() {
        @Override
        public Skin get(Player player) {
            return Skins.getPlayer(player);
        }
    };

    public interface PlayerProvider<T> {
        T get(Player player);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PlayerTabItem))
            return false;
        PlayerTabItem other = (PlayerTabItem) object;
        return this.text.equals(other.getText()) && this.skin.equals(other.getSkin()) && this.ping == other.getPing();
    }
}
