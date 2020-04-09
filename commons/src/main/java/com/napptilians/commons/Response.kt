package com.napptilians.commons

sealed class Response<out T, out E>

class Success<T>(val result: T) : Response<T, Nothing>()
class Failure<E>(val error: E) : Response<Nothing, E>()

/**
 * Then response of [T] is mapped using a [lambda] to return a new CoroutineResponse type [V].
 */
inline fun <T, V, E> Response<T, E>.map(lambda: (T) -> V): Response<V, E> {
    return when (this) {
        is Success -> {
            Success(lambda(this.result))
        }
        is Failure -> {
            Failure(this.error)
        }
    }
}

/**
 * The response of [T] is flatMapper using a [lambda] to return a new [Result] type [V].
 * e.g.: Success(List<T>) -> Success(List<V>).
 */
inline fun <T, V, E> Response<T, E>.flatMap(lambda: (T) -> Response<V, E>): Response<V, E> =
        when (this) {
            is Success -> {
                lambda(this.result)
            }
            is Failure -> {
                this
            }
        }

/**
 * Either uses [onSuccess] to define a success callback if the result is Success. [onFailure]
 * defines the action to take if the call failed.
 *
 * @return The result of the took action.
 */
inline fun <T, V, E> Response<T, E>.either(
    onSuccess: (T) -> V,
    onFailure: (E) -> V
): V =
    when (this) {
        is Success -> {
            onSuccess.invoke(this.result)
        }
        is Failure -> {
            onFailure.invoke(this.error)
        }
    }

/**
 * Returns the result if Success, null elsewhere.
 */
fun <T, E> Response<T, E>.valueOrNull(): T? =
    when (this) {
        is Success -> {
            this.result
        }
        else -> {
            null
        }
    }

/**
 * Returns the result if Success, otherwise throws an exception containing the error in Failure.
 */
fun <T, E> Response<T, E>.getOrThrow(): T =
        when (this) {
            is Success -> result
            // TODO: E should extend Throwable
            is Failure -> throw error as Throwable
        }
