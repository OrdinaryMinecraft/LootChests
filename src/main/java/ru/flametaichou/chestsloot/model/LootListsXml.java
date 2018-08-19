package ru.flametaichou.chestsloot.model;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "lootListsService")
@XmlAccessorType(XmlAccessType.FIELD)
public class LootListsXml {

    @XmlElement(name = "list")
    private List<LootList> lists = null;

    public List<LootList> getLists() {
        return lists;
    }

    public void setLists(List<LootList> lists) {
        this.lists = lists;
    }
}
