# Rate Limiter Project Documentation

## Overview

This project is primarily an explanation of how we can use the Singleton and Factory design patterns in the context of implementing a Rate Limiter. Below is a detailed explanation of both design patterns and how they contribute to building a Rate Limiter. Then, we will explore the concept of a Rate Limiter, its use cases, and explain three common types of rate limiting strategies: Fixed Window Rate Limiter, Sliding Window Rate Limiter, and Token Bucket Rate Limiter.

## Singleton Design Pattern

The Singleton Design Pattern ensures that a class has only one instance and provides a global point of access to it. This is useful when exactly one object is needed to coordinate actions across the system.

### Key Characteristics:

- **Single Instance**: Ensures that only one instance of the class is created.
- **Global Access**: The instance is globally accessible and used across different components.
- **Lazy Initialization**: The instance is created only when it is needed.

### Use in Rate Limiter:

In a Rate Limiter, we may need to manage global state (e.g., tracking API request counts). The Singleton pattern ensures that the same Rate Limiter instance is used across the entire application, preventing the creation of multiple instances that could lead to inconsistent rate limiting.

## Factory Design Pattern

The Factory Design Pattern provides an interface for creating objects in a super class but allows subclasses to alter the type of objects that will be created. This promotes flexibility and loose coupling in the code.

### Key Characteristics:

- **Abstraction**: The client code doesn't need to know the exact class that will be instantiated.
- **Encapsulation**: Object creation logic is separated from the client, simplifying the codebase.
- **Extensibility**: Makes it easy to introduce new types of objects without modifying the client code.

### Use in Rate Limiter:

In our Rate Limiter project, the Factory pattern can be used to create different types of rate limiters (e.g., Fixed Window, Sliding Window, Token Bucket). The client code doesn't need to worry about the internal details of how each rate limiter works, it just requests the appropriate type from the factory.

## What is a Rate Limiter?

A Rate Limiter is a mechanism used to control the rate at which actions (like API calls) are performed. It is primarily used in web services to prevent overloading a server or system by limiting the number of requests that a client can make in a given time period.

### Why We Use a Rate Limiter:

- **Prevent System Overload**: Protects servers from excessive load caused by too many requests.
- **Fair Usage**: Ensures fair distribution of resources among multiple users or clients.
- **Security**: Helps mitigate the risk of Distributed Denial-of-Service (DDoS) attacks.

### When We Use a Rate Limiter:

- **APIs**: To restrict the number of requests a client can make to an API within a specific time window.
- **User Actions**: To limit the number of times a user can perform certain actions (e.g., login attempts, form submissions).
- **Resource Access**: To prevent excessive usage of shared resources like database connections or storage.

## Types of Rate Limiters

### 1. Fixed Window Rate Limiter

The Fixed Window Rate Limiter divides time into fixed intervals (e.g., 1 minute, 1 hour). It counts the number of requests made by a client within the current interval, and if the limit is reached, further requests are denied until the next interval starts.

#### Characteristics:

- **Simplicity**: Easy to implement and understand.
- **Drawback**: Requests made at the end of one window and the start of the next window can lead to burst traffic.

#### Example:

If the limit is 100 requests per minute, a client can make up to 100 requests in the current minute. After that, any additional requests will be blocked until the next minute starts.

### 2. Sliding Window Rate Limiter

The Sliding Window Rate Limiter is more flexible than the Fixed Window. It tracks the request count using a rolling window, calculating the number of requests made over the last fixed interval (e.g., the last 60 seconds). This smooths out the traffic and prevents bursts at the boundary of two windows.

#### Characteristics:

- **Accuracy**: Reduces the risk of burst traffic compared to the Fixed Window.
- **Complexity**: More difficult to implement as it requires tracking requests with timestamps.

#### Example:

If the limit is 100 requests per minute, the system tracks requests over the last 60 seconds, regardless of whether they were made within one minute or across two consecutive minutes.

### 3. Token Bucket Rate Limiter

The Token Bucket Rate Limiter allows a certain number of tokens (representing requests) to be generated at a fixed rate and placed into a bucket. Each request consumes one token. When the bucket is empty, further requests are denied until more tokens are generated.

#### Characteristics:

- **Flexibility**: Allows bursts of traffic up to the bucket capacity while ensuring long-term rate limiting.
- **Efficiency**: Provides both rate limiting and the ability to handle occasional bursts.

#### Example:

If the bucket holds 100 tokens and refills at a rate of 10 tokens per second, a client can make up to 100 requests in a burst, but after the burst, the rate is throttled to 10 requests per second.

## Conclusion

In this project, we demonstrated how the Singleton and Factory design patterns can be utilized to implement various rate limiting strategies, such as Fixed Window, Sliding Window, and Token Bucket. Each of these strategies serves a unique purpose depending on the nature of traffic and the desired control over API usage.
