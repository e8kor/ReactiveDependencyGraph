# Coding Assessment StackState

## Background

### Components and dependencies

In this assessment you will code a small and simplified piece of StackState's logic pertaining to the calculation of states. StackState visualizes a undirected dependency graph of interconnected components. Each component can be connected to multiple other components. Each relation represents a dependency. The target of each relation is a component that the other component depends upon. A dependency can be uni-directional or bi-directional (meaning both components depend on each other). 

### States

A component has three types of states (properties of the component). These all can be (ordered from low to high):

 - no_data - no state is known
 - clear   - the component is running fine
 - warning - something is wrong
 - alert   - something went wrong

The three types of state are:

 1. The check states. Each components has zero, one or multiple of these. (In StackState they represent states that are calculated by running checks on monitoring data, which is outside the scope of this assessment so you can just change them yourself.) These check states can be expected to change all the time. The check states influence the own state of the component.

 1. The own state, singular, is the highest state of all check states. It is not affected by anything other then the check states. If the component has no check states the own state is no_data. (In StackState the own state represent the state that the component itself reports.) The own state of a component influences the derived state of the component

 1. The derived state, singular, is either A) the own state from warning and up or if higher B) the highest of the derived states of the components it depends on. Clear does not propagate, so if the own state of a component is clear or no_data and its dependend derived states are clear or no_data then the derived state is set to no_data. Each derived state can be traced back to an own state of a component. Thus a derived state of anything higher than clear can never be caused without an own state being higher than clear, in other words if all own states are clear all derived states must be no_data. 

### Events

It is important for our users to have traceability of events through the system. For example when Splunk reports a higher than normal error rate StackState might generate an alert check state, which becomes the new own state, which triggers a whole host of derived states to change. When a user higher up in the stack wants to know why a derived state has changed he/she will want to understand this whole chain of events. When an event causes another event it should thus retain the original event in order to capture all information.

## Simple Example

Take a dependency graph with two nodes. A component named "db" and a component named "app". The app is dependend on the db. Only the db has two check states. One of the check states named "CPU load" of the db goes to the warning state. The own state of the db becomes warning. The derived state of the db and app become warning. An event related to the derived state change of the app is able to relate this state change back to the fact that the CPU load of db became warning. 

## Assignment

Code up the above stated domain and its logic then show that for any randomly generated graph in memory with randomly changing check states over time (bonus points for using property based testing):
 
 1. The correctness of the above state logic pertaining to check states, own states and finally derived states hold after a recalculation of the states and after changes (new/removed components/dependencies) to the graph.

 1. All derived states can be traced back to the (randomly generated) event that caused a check state to change via all components that propagated the state. 

## Finally

The code should be optimized for readability. Send us your code by next week two hours before the time of the meeting to discuss the code. Try not to spend too much time; you will get a chance to defend your design choices and will score points by pointing out how your code could be better and the way you deal with any criticism you might receive. You can use the language(s) and frameworks you are most comfortable with.

If you have any questions you can reach Lodewijk at lbogaards@stackstate.com or by phone: +31653693729.