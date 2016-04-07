package Model;

import java.util.ArrayList;

import GameBoard.GameBoard;

public class LeafNodes {

	private LeafNodes root;
	private int heuristic;
	private GameBoard data;
	private LeafNodes parent;
	private ArrayList<LeafNodes> leafs;

	public LeafNodes() {
		leafs = new ArrayList<LeafNodes>();
	}
	public LeafNodes(GameBoard rootboard) {
		root = new LeafNodes();
		root.setData(rootboard);
		root.setLeafNodes(new ArrayList<LeafNodes>());
	}

	public LeafNodes getParent() {
		return parent;
	}

	public void setParent(LeafNodes parent) {
		this.parent = parent;
	}

	public GameBoard getData() {
		return data;
	}

	public void setData(GameBoard data) {
		this.data = data;
	}

	public ArrayList<LeafNodes> getLeafNodes() {
		return leafs;
	}

	public void setLeafNodes(ArrayList<LeafNodes> children) {
		this.leafs = children;
	}

	public void addLeafs(LeafNodes child) {
		leafs.add(child);
	}

	public LeafNodes getRoot() {
		return root;
	}

	public void setRoot(LeafNodes root) {
		this.root = root;
	}
	public void setHeuristic(int heuristic) {
		this.heuristic = heuristic;
	}
}
