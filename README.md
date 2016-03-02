# Efficient Range Queries

Uses a `Treap` (a binary search tree in which nodes are inserted according to id sort order)
https://en.wikipedia.org/wiki/Treap

## Algorithm
To query a range:
1. looks up the start and end of the range in the BST
2. defines a min heap ordered by node id
3. places both ends of the range and their common ancestor in the min heap
4. serves the heap minimum when `Ids.nextId()` is called and then adds node's children to heap
5. if no new node can be added (because it's not in the range) then returns `END_OF_IDS`

## Analysis
* **Space**: allocates only a heap as extra memory (produces O(k) garbage per query where k is result size)
* **Time**: does not require sorting so skips upfront k.log(k) required for sorting, still requires k.log(k) for lazily inserting k elements into min heap
* **Worst Case**: O(N + N.log(N)) time + O(N) space

Also includes, `TreeSet` and Array based implementations for the sake of comparison. The Array implementation produces less garbage than the `TreeSet` implementation however is not as efficient as the `Treap` implementation.