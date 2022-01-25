package com.yk.common.core.utils.tree;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;

/**
 * 树节点工具
 *
 * @author yzw
 */
public class TreeUtil {

    private static List<Tree> all = null;

    /**
     * 转换为树形
     *
     * @param list
     * @return
     */
    public static List<Tree> toTree(final List<Tree> list) {
        all = new ArrayList<>(list);

        // 所有根节点
        List<Tree> roots = Lists.newArrayList();

        for (Tree tree : list) {
            if (tree.getPcode() == 0) {
                roots.add(tree);
            }
        }

        // 从all中移除根节点
        all.removeAll(roots);

        for (Tree tree : roots) {
            // 递归查询子节点
            tree.setChildren(getNodeChildren(tree)); ;
        }
        return roots;
    }

    /**
     * 递归查询子节点，没有子节点时终止
     *
     * @param parent 父节点
     * @return 子节点
     */
    private static List<Tree> getNodeChildren(final Tree parent) {
        // 当前节点child为null初始化
        List<Tree> childList = parent.getChildren() == null ? new ArrayList<>() : parent.getChildren();

        // 找到当前节点的所有子节点
        for (Tree child : all) {
            if (child.getPcode() == parent.getCode()) {
                childList.add(child);
            }
        }

        // 将当前节点的所有子节点从all中删除
        all.removeAll(childList);

        // 所有的子节点再寻找它们自己的子节点
        for (Tree tree : childList) {
            tree.setChildren(getNodeChildren(tree));
        }

        return childList;
    }
}