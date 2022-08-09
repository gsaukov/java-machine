package com.apps.codility;

import java.util.*;

public class DijkstraShortestPath {

    public static void main(String[] args) {
        initGraph();
    }

    private static Graph initGraph() {
        Node nodeA = new Node("A");
        Node nodeB = new Node("B");
        Node nodeC = new Node("C");
        Node nodeD = new Node("D");
        Node nodeE = new Node("E");
        Node nodeF = new Node("F");
        Node nodeG = new Node("G");
        Node nodeH = new Node("H");
        Node nodeI = new Node("I");
        Node nodeJ = new Node("J");
        Node nodeK = new Node("K");
        Node nodeF1 = new Node("F1");
        Node nodeF2 = new Node("F2");
        Node nodeF3 = new Node("F3");

        nodeA.addNode(nodeB, 10);           //     A
        nodeA.addNode(nodeC, 15);           //   /  \
        nodeB.addNode(nodeD, 12);           //  C    B       H
        nodeB.addNode(nodeE, 15);           //  |   / \    /  \
        nodeC.addNode(nodeF, 10);           //  |  D   E--G----I
        nodeE.addNode(nodeF, 2);            //  |     /    \    \
        nodeE.addNode(nodeG, 1);            //   \   /      J----K
        nodeF.addNode(nodeF1, 5);           //     F---F1       /
        nodeF1.addNode(nodeF2, 5);          //          \      /
        nodeF2.addNode(nodeF3, 5);          //           F2--F3
        nodeF3.addNode(nodeK, 2);
        nodeG.addNode(nodeH, 9);
        nodeG.addNode(nodeI, 5);
        nodeG.addNode(nodeJ, 3);
        nodeH.addNode(nodeI, 2);
        nodeI.addNode(nodeK, 2);
        nodeJ.addNode(nodeK, 2);

        Graph graph = new Graph();

        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addNode(nodeC);
        graph.addNode(nodeD);
        graph.addNode(nodeE);
        graph.addNode(nodeF);
        graph.addNode(nodeF1);
        graph.addNode(nodeF2);
        graph.addNode(nodeF3);
        graph.addNode(nodeG);
        graph.addNode(nodeH);
        graph.addNode(nodeI);
        graph.addNode(nodeJ);
        graph.addNode(nodeK);

        return calculateShortestPathFromSource(graph, nodeA);
    }

    public static Graph calculateShortestPathFromSource(Graph graph, Node source) {
        Set<Node> settledNodes = new HashSet<>();
        Queue<Node> sortedSet = new PriorityQueue<>();
        source.distance = 0;
        sortedSet.add(source);

        while (sortedSet.size() != 0) {
            Node currentNode = sortedSet.poll();
            for (Map.Entry<Node, Integer> adjacencyPair : currentNode.adjacentNodes.entrySet()) {
                Node adjacentNode = adjacencyPair.getKey();
                Integer edgeWeight = adjacencyPair.getValue();
                if (!settledNodes.contains(adjacentNode)) {
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    sortedSet.add(adjacentNode);
                }
            }
            settledNodes.add(currentNode);
        }
        return graph;
    }

    private static void calculateMinimumDistance(Node evaluationNode, Integer edgeWeigh, Node sourceNode) {
        Integer sourceDistance = sourceNode.distance;
        if (sourceDistance + edgeWeigh < evaluationNode.distance) {
            evaluationNode.distance = (sourceDistance + edgeWeigh);
            LinkedList<Node> shortestPath = new LinkedList(sourceNode.shortestPath);
            shortestPath.add(sourceNode);
            evaluationNode.shortestPath = shortestPath;
        }
    }

    static class Node implements Comparable {
        final String name;
        Integer distance = Integer.MAX_VALUE;
        List<Node> shortestPath = new LinkedList<>();
        Map<Node, Integer> adjacentNodes = new HashMap<>();

        Node(String name) {
            this.name = name;
        }

        void addNode(Node node, Integer distanceTo) {
            adjacentNodes.put(node, distanceTo);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return Objects.equals(name, node.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

        @Override
        public int compareTo(Object o) {
            if (o == null || getClass() != o.getClass()) throw new IllegalArgumentException();
            Node node = (Node) o;
            return distance.compareTo(node.distance);
        }
    }

    static class Graph {
        private Set<Node> nodes = new HashSet<>();

        public void addNode(Node nodeA) {
            nodes.add(nodeA);
        }
    }
}
