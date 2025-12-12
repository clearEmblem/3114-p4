# CS3114 Project 4 Video Script

**Target Duration:** ~5 Minutes

---

## 1. Project Overview (0:00 - 0:40)
*Action: Display Project Assignment PDF or Eclipse Package Explorer*

"Hi, this is **[Your Name]**, and this is my video for the **Air Traffic Control project**.

The objective of this project was to create a database management system for aircraft in a 3D space. The core challenge was efficiently managing these objects in two ways simultaneously:
1.  By **name** using a **SkipList**.
2.  By **physical location** using a **3D BinTree**.

Initially, I found understanding the 3D interpretation of the BinTree confusing—specifically how the splitting dimensions cycle compared to a standard 2D quadtree. However, once I visualized the box splitting sequentially from **X to Y to Z**, the implementation became much clearer."

---

## 2. Class Structure (0:40 - 2:30)
*Action: Open `WorldDB.java`, then browse `InternalNode.java` and `LeafNode.java`*

"Let’s walk through the class structure. I designed this using a few key patterns to keep the code clean.

**Facade Pattern (`WorldDB.java`)**
`WorldDB` acts as my main facade. It handles all input validation and coordinates actions between the SkipList and the Bintree. This separation of concerns ensures the data structures don't strictly depend on each other; they just store shared object references.

**Composite Pattern (The Bintree)**
For the Bintree, I used the Composite Design Pattern with an interface `BinNode` and three implementations:
1.  **`InternalNode`**: Handles the recursive logic of splitting the world and delegating work to children.
2.  **`LeafNode`**: Stores the actual `AirObjects`. I used a LinkedList approach here to store objects that share a region.
3.  **`EmptyLeafNode`**: A **Flyweight** implementation. Since all empty nodes are identical, I use a single static instance to save memory instead of creating thousands of empty objects.

.**Inheritance (`AirObject`)**
Finally, I have `AirObject` as an abstract parent class with subclasses like `Drone` and `Airplane`. This polymorphism lets me treat all aircraft uniformly when checking for collisions."

---

## 3. Implementation & Key Algorithms (2:30 - 3:50)
*Action: Open `InternalNode.java` or `BoxUtil.java`*

"Moving on to implementation details. The primary data structure for spatial queries is the **3D BinTree**.

**The Origin Rule**
A key deviation from standard implementations is how I handled the Project Rubric's specific Intersection rules. The rubric requires that we don't report the same object multiple times for a single query, even if it spans multiple nodes.

To solve this efficiently, I implemented the **'Origin Rule'**. In my `LeafNode`, I only report an intersection if the *origin* of the intersection box falls within that specific leaf node's region. This ensures that even if an airplane overlaps three nodes, it is only properly 'counted' by exactly one node.

**Time Complexity**
*   **SkipList**: Operations are expected to be **O(log n)** on average due to randomized leveling.
*   **Bintree**: Spatial queries are generally logarithmic with respect to the volume of space. However, in worst-case scenarios (like all objects stacking in one spot), it could degrade to linear time relative to the number of objects in that crowded node."

---

## 4. Mutation Testing (3:50 - 4:45)
*Action: Open `CoverageTest.java`*

"For testing, I didn't just rely on the public tests. I built a comprehensive suite called `CoverageTest.java`.

**Methodology**
My methodology was to target logic-heavy classes first. For `BoxUtil`, which contains all the math for overlaps, I wrote specific tests for **'off-by-one'** errors—explicitly distinguishing between objects that validly touch versus those that strictly overlap.

**Edge Cases**
A significant edge case I encountered was **Collision Reporting**. I initially had issues where recursive calls would report the same collision pair twice (A hits B, and B hits A). To fix this, I added logic to ensure we only report the pair once by enforcing a consistent ordering. I verified this by running mutation tests; changing any comparison operator caused an immediate test failure, confirming my coverage."

---

## 5. AI/LLM Usage (4:45 - 5:30)
*Action: Switch to Canvas or keep code visible*

"Finally, regarding LLM usage—I utilized an AI assistant for this project, primarily acting as a **pair programmer**.

It was incredibly helpful for two specific things:
1.  **Debugging Checkstyle Errors**: It saved me hours fixing indentation and brace placement to meet the strict course style guide.
2.  **Generating Test Boilerplate**: It helped generate skeleton code for unit tests, allowing me to focus my energy on the complex logic tests for the Bintree recursion.

**Challenges**
However, I did encounter problems where the AI would sometimes suggest using standard Java library features (like `HashSet` for tracking duplicates) that were **explicitly forbidden** by the project rules. I had to manually catch these and refactor the design to use the 'Origin Rule' algorithm instead."

---

### Closing
"That covers my implementation. Thank you for watching."
