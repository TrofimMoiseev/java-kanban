package service;

import model.*;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private Node<Task> head;
    private Node<Task> tail;
    private final Map<Integer, Node<Task>> history = new HashMap<>();

    private void linkLast(Task task) {
        Node<Task> newNode;
        Node<Task> oldTail = tail;

        if (oldTail == null) {
            newNode = new Node<>(null, task, null);
            head = newNode;
        } else {
            newNode = new Node<>(oldTail, task, null);
            oldTail.next = newNode;
        }
        tail = newNode;

        Task t = newNode.task;
        history.put(t.getId(), newNode);
    }

    private List<Task> getTasks() {
        List<Task> listHistory = new ArrayList<>();
        Node<Task> t = head;
        while (t != null) {
            listHistory.add(t.task);
            t = t.next;
        }
        return listHistory;
    }

    private void removeNode(Node<Task> oldNode) {
        Node<Task> prevNode = oldNode.prev;
        Node<Task> nextNode = oldNode.next;
        if (nextNode == null && prevNode == null) {
            head = null;
            tail = null;
        } else {
            if (prevNode == null) {
                nextNode.prev = null;
                head = nextNode;
            } else if (nextNode == null) {
                prevNode.next = null;
                tail = prevNode;
            } else {
                prevNode.next = nextNode;
                nextNode.prev = prevNode;
            }
        }
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            remove(task.getId());
            linkLast(task);
        }
    }

    @Override
    public void remove(int id) {
        if (history.containsKey(id)) {
            removeNode(history.get(id));
            history.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }
}
