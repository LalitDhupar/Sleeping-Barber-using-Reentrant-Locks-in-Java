# Sleeping-Barber-using-Reentrant-Locks-in-Java

# Problem Statement: 
A small barber shop has two doors, an entrance and an exit. Inside is a set of M barbers who spends all their lives serving customers, one at a time. Each barber has chair in which the customer sits when they are getting their hair cut. When there are no customers in the shop waiting for their hair to be cut, a barber sleeps in his chair. Customer arrive at random intervals, with mean mc and standard deviation sdc. If a customer arrives and finds the barber asleep, he awakens the barber, sits in the barber’s chair and sleeps while his hair is being cut. The time taken to cut a customer's hair has a mean mh and standard deviation sdh. If a customer arrives and all the barbers are busy cutting hair, the customer goes asleep in one of the N waiting chairs. When the barber finishes cutting a customer’s hair, he awakens the customer and holds the exit door open for him. If there are any waiting customers, he awakens one and waits for the customer to sit in the barber's chair, otherwise he goes to sleep.
Implement a multi-threaded, multi-core solution in Java. This solution should be efficient with respect to all actors. The solution should demonstrate the standard safety properties (mutual exclusion, absence of deadlock) and liveness properties (absence of starvation, fairness).

# Approach
Reentrant locks have been used for solving this problem by maintining the safety and liveliness properties of the code.
