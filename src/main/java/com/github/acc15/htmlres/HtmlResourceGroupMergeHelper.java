package com.github.acc15.htmlres;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by acc15 on 12.03.16.
 */
public class HtmlResourceGroupMergeHelper {

    private final Map<String, HtmlResourceGroup> groupMap = new HashMap<>();
    private final Set<HtmlResourceGroup> flatGroups = new HashSet<>();
    private final ResourceGroup rootGroup;

    public HtmlResourceGroupMergeHelper(ResourceGroup rootGroup) {
        this.rootGroup = rootGroup;
    }

    public void mergeGroups(Iterable<HtmlResourceGroup> groups) {
        for (HtmlResourceGroup group: groups) {
            if (group.getId() != null) {
                groupMap.put(group.getId(), group);
            }
        }
        for (HtmlResourceGroup group: groups) {
            flattenGroup(group);
        }

    }

    private void flattenGroup(HtmlResourceGroup group) {
        if (flatGroups.contains(group)) {
            // group already flat
            return;
        }
        ResourceGroup parent = rootGroup;
        if (group.getParent() != null) {
            final HtmlResourceGroup parentGroup = groupMap.get(group.getParent());
            if (parentGroup == null) {
                throw new IllegalArgumentException("illegal parent group id: " + group.getParent());
            }
            flattenGroup(parentGroup);
            parent = parentGroup;
        }
        group.merge(parent);

        flatGroups.add(group);
    }

}
