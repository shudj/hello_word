package com.jl.test.scala.rbTree;

public class RBTree<T extends Comparable<T>> {

    public static boolean RED = true;
    public static boolean BLACK = false;
    private RBNode<T> root;    // 根结点
    public class RBNode<T extends Comparable<T>> {
        // yan se
        boolean color;
        // guan jian zhi(jian zhi)
        T key;
        // zuo zi jie dian
        RBNode left;
        // you zi jie dian
        RBNode right;
        // fu jie dian
        RBNode parent;

        public RBNode(boolean color, T key, RBNode left, RBNode right, RBNode parent) {
            this.color = color;
            this.key = key;
            this.left = left;
            this.right = right;
            this.parent = parent;
        }

        public T getKey() {
            return this.key;
        }

        public String toString() {
            return "" + key + (this.color == RED ? "R" : "B");
        }
    }


    /**
     * zuo xuan zuo san jian shi
     * 1. jiang y de zuo jie dian fu gei x de you zi jie dian, bing jiang x fu gei y zuo jie zi jie dian
     *    de fu jie dian(y zuo zi jie dian wei fei kong shi)
     * 2. jiang x de fu jie dian p(wei fei kong shi)fu gei y de fu jie dian, tong shi geng xin p de zi jie
     *    dian wei y(zuo huo zhe you)
     * 3. jiang y de zuo zi jie dian she wei x, jiang x de fu jie dian she wei y
     * @param x
     */
    private void leftRotate(RBNode x) {
            /*
                1. jiang y de zuo jie dian fu gei x de you zi jie dian, bing jiang x fu gei y zuo jie zi jie dian
                de fu jie dian(y zuo zi jie dian wei fei kong shi)
             */
        RBNode y = x.right;
        x.right = y.left;
        if (null != y.left) {
            y.left.parent = x;
        }

            /*
                2. jiang x de fu jie dian p(wei fei kong shi)fu gei y de fu jie dian, tong shi geng xin p de zi jie
                dian wei y(zuo huo zhe you)
             */
        y.parent = x.parent;
        if (null == x.parent) {
            this.root = y;
        } else {
            if (x == x.parent.left) {
                x.parent.left = y;
            } else {
                x.parent.right = y;
            }
        }

        // 3. jiang y de zuo zi jie dian she wei x, jiang x de fu jie dian she wei y
        y.left = x;
        x.parent = y;
    }

    // you xuan
    private void rightRotate(RBNode y) {
        RBNode x = y.left;
        y.left = x.right;
        if (null != x.right) {
            x.right.parent = y;
        }

        x.parent = y.parent;
        if (null == y.parent) {
            this.root = x;
        } else {
            if (y == y.parent.right) {
                y.parent.right = x;
            } else {
                y.parent.left = x;
            }
        }

        x.right = y;
        y.parent = x;
    }

    /**
     * cha ru
     * @param key
     */
    private void insert(T key) {
        RBNode node = new RBNode(RED, key,null, null, null);
        if (null != node) {
            insert(node);
        }
    }

    private void insert(RBNode node) {
        // biao shi zui hou node de fu jie dian
        RBNode current = null;
        // yong lai xiang xia shou suo yong de
        RBNode x = this.root;

        // 1. zhao dao cha ru wei zhi
        while(x != null) {
            current = x;
            int cmp = node.key.compareTo(x.key);
            if (cmp < 0) {
                x = x.left;
            } else {
                x = x. right;
            }
        }
        // zhao dao wei zhi,jiang dang qian current zuo wei node de fu jie dian
        node.parent = current;

        //2. jie xia lai pan duan node shi cha ru zai zuo zi jie dian hai shi you zi jie dian
        if (null != current) {
            int cmp = node.key.compareTo(current.key);
            if (cmp < 0) {
                current.left = node;
            } else {
                current.right = node;
            }
        } else {
            this.root = node;
        }

        // 3. jiang ta chong xin xiu zheng wei yi ke hong hei shu
        insertFixUp(node);
    }

    private void insertFixUp(RBNode node) {
        // ding yi fu jie dian he zu fu jie dian
        RBNode parent, gParent;

        // xu yao xiu zheng de tiao jian: fu jie dian cun zai,qie fu jie dian de yan se shi hong se
        while (((parent = parentOf(node)) != null) && isRed(parent)) {
            // huo de zu fu jie dian
            gParent = parentOf(parent);
            // ruo fu jie dian shi zu fu jie dian de zuo zi jie dian, xia mian else yu qi xiang fan
            if (parent == gParent.left) {
                // huo de shu shu jie dian
                RBNode uncle = gParent.right;
                // 1. shu shu jie dian ye shi hong se
                if (uncle != null && isRed(uncle)) {
                    setBlack(parent);
                    setBlack(uncle);
                    setRed(gParent);
                    // jiang jie dian wei zhi fang zai zu fu jie dian shang
                    node = gParent;
                    continue;
                }

                // 2. shu shu jie dian shi hei se, qie dang qian jie dian shi you zi jie dian
                if (node == parent.right) {
                    leftRotate(parent);
                    // ran hou jiang  fu jie dian he zi diao huan yi xia ,wei xia mian you xuan zuo zhun bei
                    RBNode tmp = parent;
                    parent = node;
                    node = tmp;
                }

                // 3. shu shu jie dian shi hei se, qie dang qian jie dian shi zuo zi jie dian
                setBlack(parent);
                setRed(gParent);
                rightRotate(gParent);
            } else {
                // ruo fu jie dian shi zu fu jie dian de you zi jie dian
                RBNode uncle = gParent.left;
                // 1. shu shu jie dian shi hong se
                if (null != uncle && isRed(uncle)) {
                    setBlack(parent);
                    setBlack(uncle);
                    setRed(gParent);
                    node = gParent;
                    continue;
                }

                // 2. shu shu jie dian shi hei se,qie dang qiang jie dian shi zuo zi jie dian
                if (node == parent.left) {
                    rightRotate(parent);
                    RBNode tmp = parent;
                    parent = node;
                    node = tmp;
                }

                // 3. shu shu jie dian shi hei se de, qie dang qian shi you zi jie dian
                setBlack(parent);
                setRed(gParent);
                leftRotate(gParent);
            }
        }
        // jiang gen jie dian she wei hei se
        setBlack(this.root);
    }

    private void setRed(RBNode node) {
        if (null != node) {
            node.color = RED;
        }
    }

    private void setBlack(RBNode node) {
       if (null != node) {
           node.color = BLACK;
       }
    }

    private boolean isRed(RBNode node) {
        return ((node!=null)&&(node.color==RED)) ? true : false;
    }

    private RBNode parentOf(RBNode node) {
        return node != null ? node.parent : null;
    }
}