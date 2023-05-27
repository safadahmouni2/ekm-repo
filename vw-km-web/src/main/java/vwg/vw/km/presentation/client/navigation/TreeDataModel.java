package vwg.vw.km.presentation.client.navigation;

import java.util.List;

import javax.swing.tree.TreeNode;

import org.icefaces.ace.model.tree.ListNodeDataModel;

public class TreeDataModel extends ListNodeDataModel {

	private static final long serialVersionUID = -8844254817078602882L;

	private TreeNode rootNode;

	public TreeDataModel(List<TreeNode> treeRoots) {
		super(treeRoots);
	}

	public TreeDataModel(TreeNode root) {
		super(root);
		setData(root);
		setRootNode(root);
	}

	public TreeNode getRootNode() {
		return rootNode;
	}

	public void setRootNode(TreeNode rootNode) {
		this.rootNode = rootNode;
	}

}
