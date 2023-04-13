package com.github.laefye.plugincinema.api.ui;

import com.github.laefye.plugincinema.PluginCinema;
import com.github.laefye.plugincinema.utils.ItemMetaWrapper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class MenuWithPages<T> extends Menu {
    protected Pages<T> pages;

    public MenuWithPages(ArrayList<T> elements, String title) {
        super(title);
        pages = new Pages<>(elements, 9*5);
        setItems();
    }

    private void setItems() {
        inventory.clear();
        for (int i = 0; i < pages.getCountInPage(); i++) {
            inventory.setItem(i, setItem(i, pages.get(i)));
        }
        if (pages.hasNext()) {
            inventory.setItem(9 * 5 + 8, new ItemMetaWrapper(new ItemStack(Material.GREEN_DYE))
                    .setDisplayName(PluginCinema.getInstance().lang.getPhraseWithReset("video.menu.next_page"))
                    .apply());
        }
        if (pages.hasPrevious()) {
            inventory.setItem(9 * 5, new ItemMetaWrapper(new ItemStack(Material.RED_DYE))
                    .setDisplayName(PluginCinema.getInstance().lang.getPhrase("video.menu.previous_page"))
                    .apply());
        }
        if (pages.getCountPages() > 1){
            inventory.setItem(9 * 5 + 4, new ItemMetaWrapper(new ItemStack(Material.BOOK, pages.getCurrentPage()))
                    .setDisplayName(PluginCinema.getInstance().lang.getPhraseFormattedWithReset("video.menu.current_page", pages.getCurrentPage()))
                    .apply()
            );
        }
    }

    protected ItemStack setItem(int slot, T element) {
        return null;
    }

    public void onClick(Player player, int slot, T element) {

    }

    @Override
    public void onClick(Player player, int slot) {
        if (slot < pages.getCountInPage()) {
            onClick(player, slot, pages.get(slot));
        }
        if (slot == 9 * 5 + 8 && pages.hasNext()) {
            pages.nextPage();
            setItems();
        }
        if (slot == 9 * 5 && pages.hasPrevious()) {
            pages.previousPage();
            setItems();
        }
    }
}
