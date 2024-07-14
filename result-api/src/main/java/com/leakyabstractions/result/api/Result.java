/*
 * Copyright 2024 Guillermo Calvo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.leakyabstractions.result.api;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Encapsulates a single non-null value, representing the outcome of an operation that can either <em>succeed</em> or
 * <em>fail</em>.
 * <ul>
 * <li><strong>Successful results</strong> hold a value of type {@code S}, indicating that the operation <em>has
 * completed as intended</em>.
 * <li><strong>Failed results</strong> hold a value of type {@code F}, indicating that the operation <em>did not
 * complete as intended</em>.
 * </ul>
 * <p>
 * The exact definitions of <em>success</em> and <em>failure</em> depend on the semantics of the operation.
 *
 * @apiNote {@code Result} is primarily, but not only, intended for use as a method return type to handle anticipated
 *     failures without throwing exceptions or returning {@code null}.
 * @implSpec This is a
 *     <a href="https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/doc-files/ValueBased.html">
 *     value-based</a> type; use of identity-sensitive operations on instances of {@code Result} should be avoided.
 * @author <a href="https://guillermo.dev/">Guillermo Calvo</a>
 * @see com.leakyabstractions.result.api
 * @param <S> the type of the success value
 * @param <F> the type of the failure value
 */
@SuppressWarnings("unused")
public interface Result<S, F> {

    /**
     * Checks if this {@code Result} is successful.
     * <p>
     * <img src="doc-files/hasSuccess.svg" alt="">
     *
     * <pre class="row-color rowColor">
     * <code>&nbsp;
     * Result&lt;Integer, String&gt; r = getResult();
     * boolean x = r.hasSuccess();</code>
     * </pre>
     *
     * @return if this {@code Result} is successful, {@code true}; otherwise, {@code false}
     * @see #hasFailure()
     */
    boolean hasSuccess();

    /**
     * Checks if this {@code Result} is failed.
     * <p>
     * <img src="doc-files/hasFailure.svg" alt="">
     *
     * <pre class="row-color rowColor">
     * <code>&nbsp;
     * Result&lt;Integer, String&gt; r = getResult();
     * boolean x = r.hasFailure();</code>
     * </pre>
     *
     * @return if this {@code Result} is failed, {@code true}; otherwise, {@code false}
     * @see #hasSuccess()
     */
    boolean hasFailure();

    /**
     * Returns this {@code Result}'s success value as a possibly-empty {@link Optional}.
     * <p>
     * <img src="doc-files/getSuccess.svg" alt="">
     *
     * <pre class="row-color rowColor">
     * <code>&nbsp;
     * Result&lt;Integer, String&gt; r = getResult();
     * Optional&lt;Integer&gt; x = r.getSuccess();</code>
     * </pre>
     *
     * @return if this {@code Result} is successful, an {@link Optional} containing its value; otherwise, an empty
     *     {@code Optional}
     * @see #getFailure()
     */
    Optional<S> getSuccess();

    /**
     * Returns this {@code Result}'s failure value as a possibly-empty {@link Optional}.
     * <p>
     * <img src="doc-files/getFailure.svg" alt="">
     *
     * <pre class="row-color rowColor">
     * <code>&nbsp;
     * Result&lt;Integer, String&gt; r = getResult();
     * Optional&lt;String&gt; x = r.getFailure();</code>
     * </pre>
     *
     * @return if this {@code Result} is failed, an {@link Optional} containing its value; otherwise, an empty
     *     {@code Optional}
     * @see #getSuccess()
     */
    Optional<F> getFailure();

    /**
     * Returns this {@code Result}'s success value, or the alternative one.
     * <p>
     * <img src="doc-files/orElse.svg" alt="">
     *
     * <pre class="row-color rowColor">
     * <code>&nbsp;
     * Result&lt;Integer, String&gt; r = getResult();
     * int x = r.orElse(8);</code>
     * </pre>
     *
     * @param other the alternative success value; may be {@code null}
     * @return if this {@code Result} is successful, its value; otherwise {@code other}
     * @see #orElseMap(Function)
     */
    S orElse(S other);

    /**
     * Returns this {@code Result}'s success value, or maps its failure value.
     * <p>
     * If this {@code Result} is failed, {@code mapper} will be applied to its value to produce an alternative success
     * value.
     * <p>
     * <img src="doc-files/orElseMap.svg" alt="">
     *
     * <pre class="row-color rowColor">
     * <code>&nbsp;
     * Result&lt;Integer, String&gt; r = getResult();
     * int x = r.orElseMap(f -&gt; 8);</code>
     * </pre>
     *
     * @param mapper the mapping {@link Function} that produces the alternative success value; may return {@code null}
     * @return if this {@code Result} is successful, its value; otherwise the value produced by {@code
     *     mapper}
     * @throws NullPointerException if this {@code Result} is failed and {@code mapper} is {@code
     *     null}
     * @see #orElse(Object)
     */
    S orElseMap(Function<? super F, ? extends S> mapper);

    /**
     * Returns this {@code Result}'s success value as a possibly-empty {@link Stream}.
     * <p>
     * <img src="doc-files/streamSuccess.svg" alt="">
     *
     * <pre class="row-color rowColor">
     * <code>&nbsp;
     * Result&lt;Integer, String&gt; r = getResult();
     * Stream&lt;Integer&gt; x = r.streamSuccess();</code>
     * </pre>
     *
     * @return If this {@code Result} is successful, a sequential {@link Stream} containing only its value; otherwise an
     *     empty {@code Stream}
     */
    Stream<S> streamSuccess();

    /**
     * Returns this {@code Result}'s failure value as a possibly-empty {@link Stream}.
     * <p>
     * <img src="doc-files/streamFailure.svg" alt="">
     *
     * <pre class="row-color rowColor">
     * <code>&nbsp;
     * Result&lt;Integer, String&gt; r = getResult();
     * Stream&lt;String&gt; x = r.streamFailure();</code>
     * </pre>
     *
     * @return if this {@code Result} is failed, a sequential {@link Stream} containing only its value; otherwise an
     *     empty {@code Stream}
     */
    Stream<F> streamFailure();

    /**
     * Performs the given action with this {@code Result}'s success value.
     * <p>
     * If this {@code Result} is successful, performs {@code action} with its value; otherwise does nothing.
     * <p>
     * <img src="doc-files/ifSuccess.svg" alt="">
     *
     * <pre class="row-color rowColor">
     * <code>&nbsp;
     * Result&lt;Integer, String&gt; r = getResult();
     * Result&lt;Integer, String&gt; x = r.ifSuccess(System.out::println);</code>
     * </pre>
     *
     * @param action the {@link Consumer} to be applied to this {@code Result}'s success value
     * @throws NullPointerException if this {@code Result} is successful and {@code action} is {@code
     *     null}
     * @return this {@code Result}
     * @see #ifFailure(Consumer)
     * @see #ifSuccessOrElse(Consumer, Consumer)
     */
    Result<S, F> ifSuccess(Consumer<? super S> action);

    /**
     * Performs the given action with this {@code Result}'s failure value.
     * <p>
     * If this {@code Result} is failed, performs {@code action} with its value; otherwise does nothing.
     * <p>
     * <img src="doc-files/ifFailure.svg" alt="">
     *
     * <pre class="row-color rowColor">
     * <code>&nbsp;
     * Result&lt;Integer, String&gt; r = getResult();
     * Result&lt;Integer, String&gt; x = r.ifFailure(System.err::println);</code>
     * </pre>
     *
     * @param action the {@link Consumer} to be applied to this {@code Result}'s failure value
     * @return this {@code Result}
     * @throws NullPointerException if this {@code Result} is failed and {@code action} is {@code
     *     null}
     * @see #ifSuccess(Consumer)
     * @see #ifSuccessOrElse(Consumer, Consumer)
     */
    Result<S, F> ifFailure(Consumer<? super F> action);

    /**
     * Performs either of the given actions with this {@code Result}'s value.
     * <p>
     * If this {@code Result} is successful, performs {@code successAction}; otherwise performs {@code failureAction}.
     * <p>
     * <img src="doc-files/ifSuccessOrElse.svg" alt="">
     *
     * <pre class="row-color rowColor">
     * <code>&nbsp;
     * Result&lt;Integer, String&gt; r = getResult();
     * Result&lt;Integer, String&gt; x = r.ifSuccessOrElse(System.out::println, System.err::println);</code>
     * </pre>
     *
     * @param successAction the {@link Consumer} to be applied to this {@code Result}'s success value
     * @param failureAction the {@link Consumer} to be applied to this {@code Result}'s failure value
     * @return this {@code Result}
     * @throws NullPointerException if this {@code Result} is successful and {@code successAction} is {@code null}; or
     *     if it is failed and {@code failureAction} is {@code null}
     * @see #ifFailure(Consumer)
     * @see #ifSuccess(Consumer)
     */
    Result<S, F> ifSuccessOrElse(
            Consumer<? super S> successAction, Consumer<? super F> failureAction);

    /**
     * Transforms this successful {@code Result} into a failed one, based on the given condition.
     * <p>
     * If this is a successful {@code Result} whose value does not satisfy {@code isAcceptable}, {@code mapper} will be
     * applied to the value to produce a failure value.
     * <p>
     * <img src="doc-files/filter.svg" alt="">
     *
     * <pre class="row-color rowColor">
     * <code>&nbsp;
     * Result&lt;Integer, String&gt; r = getResult();
     * Result&lt;Integer, String&gt; x = r.filter(s -&gt; s &lt; 3, s -&gt; "E");</code>
     * </pre>
     *
     * @param isAcceptable the {@link Predicate} to apply to this {@code Result}'s success value
     * @param mapper the mapping {@link Function} that produces the failure value
     * @return if this is a successful {@code Result} whose value is deemed not acceptable, a new failed {@code Result}
     *     holding the value produced by {@code mapper}; otherwise, this {@code
     *     Result}
     * @throws NullPointerException if this {@code Result} is successful and {@code isAcceptable} is {@code null}; or if
     *     its success value is not acceptable and {@code mapper} is {@code null} or returns {@code null}
     * @see #recover(Predicate, Function)
     */
    Result<S, F> filter(Predicate<? super S> isAcceptable, Function<? super S, ? extends F> mapper);

    /**
     * Transforms this failed {@code Result} into a successful one, based on the given condition.
     * <p>
     * If this is a failed {@code Result} whose value satisfies {@code isRecoverable}, {@code
     * mapper} will be applied to the value to produce a success value.
     * <p>
     * <img src="doc-files/recover.svg" alt="">
     *
     * <pre class="row-color rowColor">
     * <code>&nbsp;
     * Result&lt;Integer, String&gt; r = getResult();
     * Result&lt;Integer, String&gt; x = r.recover("B"::equals, f -&gt; 5);</code>
     * </pre>
     *
     * @param isRecoverable the {@link Predicate} to apply to this {@code Result}'s failure value
     * @param mapper the mapping {@link Function} that produces the success value
     * @return if this is a failed {@code Result} whose value is deemed recoverable, a new successful {@code Result}
     *     holding the value produced by {@code mapper}; otherwise, this {@code Result}
     * @throws NullPointerException if this {@code Result} is failed and {@code isRecoverable} is {@code null}; or if
     *     its failure value is recoverable and {@code mapper} is {@code null} or returns {@code null}
     * @see #filter(Predicate, Function)
     */
    Result<S, F> recover(Predicate<? super F> isRecoverable, Function<? super F, ? extends S> mapper);

    /**
     * Transforms this {@code Result}'s success value.
     * <p>
     * If this {@code Result} is successful, {@code mapper} will be applied to its value to produce a new one, which may
     * differ in type.
     * <p>
     * <img src="doc-files/mapSuccess.svg" alt="">
     *
     * <pre class="row-color rowColor">
     * <code>&nbsp;
     * Result&lt;Integer, String&gt; r = getResult();
     * Result&lt;Fruit, String&gt; x = r.mapSuccess(s -&gt; CHERRIES);</code>
     * </pre>
     *
     * @param <S2> the type of the value returned by {@code mapper}
     * @param mapper the mapping {@link Function} that produces the new success value
     * @return if this is a successful {@code Result}, a new successful {@code Result} holding the value produced by
     *     {@code mapper}; otherwise, this {@code Result}
     * @throws NullPointerException if this {@code Result} is successful and {@code mapper} is {@code
     *     null} or returns {@code null}
     * @see #map(Function, Function)
     * @see #mapFailure(Function)
     */
    <S2> Result<S2, F> mapSuccess(Function<? super S, ? extends S2> mapper);

    /**
     * Transforms this {@code Result}'s failure value.
     * <p>
     * If this {@code Result} is failed, {@code mapper} will be applied to its value to produce a new one, which may
     * differ in type.
     * <p>
     * <img src="doc-files/mapFailure.svg" alt="">
     *
     * <pre class="row-color rowColor">
     * <code>&nbsp;
     * Result&lt;Integer, String&gt; r = getResult();
     * Result&lt;Integer, Suit&gt; x = r.mapFailure(f -&gt; CLUBS);</code>
     * </pre>
     *
     * @param <F2> the type of the value returned by {@code mapper}
     * @param mapper the mapping {@link Function} that produces the new failure value
     * @return if this is a failed {@code Result}, a new failed {@code Result} holding the value produced by
     *     {@code mapper}; otherwise, this {@code Result}
     * @throws NullPointerException if this {@code Result} is failed and {@code mapper} is {@code
     *     null} or returns {@code null}
     * @see #map(Function, Function)
     * @see #mapSuccess(Function)
     */
    <F2> Result<S, F2> mapFailure(Function<? super F, ? extends F2> mapper);

    /**
     * Transforms this {@code Result}'s success or failure value.
     * <p>
     * If this is {@code Result} is successful, {@code successMapper} will be applied to its value to produce a new one.
     * Otherwise, {@code failureMapper} will be applied to its failure value to produce a new one.
     * <p>
     * Both success and failure values may differ in type.
     * <p>
     * <img src="doc-files/map.svg" alt="">
     *
     * <pre class="row-color rowColor">
     * <code>&nbsp;
     * Result&lt;Integer, String&gt; r = getResult();
     * Result&lt;Fruit, Suit&gt; x = r.map(s -&gt; CHERRIES, f -&gt; CLUBS);</code>
     * </pre>
     *
     * @param <S2> the type of the value returned by {@code successMapper}
     * @param <F2> the type of the value returned by {@code failureMapper}
     * @param successMapper the mapping {@link Function} that produces the new success value
     * @param failureMapper the mapping {@link Function} that produces the new failure value
     * @return if this is a successful {@code Result}, a new successful {@code Result} holding the value produced by
     *     {@code successMapper}; otherwise, a new failed {@code Result} holding the value produced by
     *     {@code failureMapper}
     * @throws NullPointerException if this result is successful and {@code successMapper} is {@code
     *     null} or returns {@code null}; or if this {@code Result} is failed and {@code
     *     failureMapper} is {@code null} or returns {@code null}
     * @see #mapFailure(Function)
     * @see #mapSuccess(Function)
     */
    <S2, F2> Result<S2, F2> map(
            Function<? super S, ? extends S2> successMapper,
            Function<? super F, ? extends F2> failureMapper);

    /**
     * Transforms this successful {@code Result} into a different one.
     * <p>
     * If this {@code Result} is successful, {@code mapper} will be applied to its value to produce a new
     * {@code Result}, which may now hold either a success or a failure value. New success value may differ in type.
     * <p>
     * <img src="doc-files/flatMapSuccess.svg" alt="">
     *
     * <pre class="row-color rowColor">
     * <code>&nbsp;
     * Result&lt;Integer, String&gt; r = getResult();
     * Result&lt;Fruit, String&gt; x = r.flatMapSuccess(s -&gt; s &lt; 3 ? success(CHERRIES) : failure("E"));</code>
     * </pre>
     *
     * @param <S2> the success type of the {@code Result} returned by {@code mapper}
     * @param mapper the mapping {@link Function} that produces a new {@code Result}
     * @return if this {@code Result} is successful, a new {@code Result} produced by {@code mapper}; otherwise, this
     *     {@code Result}
     * @throws NullPointerException if this {@code Result} is successful and {@code mapper} is {@code
     *     null} or returns {@code null}
     * @see #flatMap(Function, Function)
     * @see #flatMapFailure(Function)
     */
    <S2> Result<S2, F> flatMapSuccess(
            Function<? super S, ? extends Result<? extends S2, ? extends F>> mapper);

    /**
     * Transforms this failed {@code Result} into a different one.
     * <p>
     * If this {@code Result} is failed, {@code mapper} will be applied to its value to produce a new {@code Result},
     * which may now hold either a success or a failure value. New failure value may differ in type.
     * <p>
     * <img src="doc-files/flatMapFailure.svg" alt="">
     *
     * <pre class="row-color rowColor">
     * <code>&nbsp;
     * Result&lt;Integer, String&gt; r = getResult();
     * Result&lt;Integer, Suit&gt; x = r.flatMapFailure(f -&gt; f.equals("B") ? success(5) : failure(CLUBS));</code>
     * </pre>
     *
     * @param <F2> the failure type of the {@code Result} returned by {@code mapper}
     * @param mapper the mapping {@link Function} that produces a new {@code Result}
     * @return if this {@code Result} is failed, a new {@code Result} produced by {@code mapper}; otherwise, this
     *     {@code Result}
     * @throws NullPointerException if this {@code Result} is failed and {@code mapper} is {@code
     *     null} or returns {@code null}
     * @see #flatMap(Function, Function)
     * @see #flatMapSuccess(Function)
     */
    <F2> Result<S, F2> flatMapFailure(
            Function<? super F, ? extends Result<? extends S, ? extends F2>> mapper);

    /**
     * Transforms this {@code Result} into a different one.
     * <p>
     * If this {@code Result} is successful, {@code successMapper} will be applied to its value to produce a new
     * {@code Result}; otherwise, {@code failureMapper} will be applied to its value to produce a new {@code Result}.
     * <p>
     * The new {@code Result} may now hold either a success or a failure value. Both may differ in type.
     * <p>
     * <img src="doc-files/flatMap.svg" alt="">
     *
     * <pre class="row-color rowColor">
     * <code>&nbsp;
     * Result&lt;Integer, String&gt; r = getResult();
     * Result&lt;Fruit, Suit&gt; x = r.flatMap(s -&gt; s &lt; 3 ? success(CHERRIES) : failure(SPADES),
     *     f -&gt; f.equals("B") ? success(WATERMELON) : failure(CLUBS));</code>
     * </pre>
     *
     * @param <S2> the success type of the {@code Result} returned by {@code successMapper} and {@code
     *     failureMapper}
     * @param <F2> the failure type of the {@code Result} returned by {@code successMapper} and {@code
     *     failureMapper}
     * @param successMapper the mapping {@link Function} that produces a new {@code Result} if this {@code Result} is
     *     successful
     * @param failureMapper the mapping {@link Function} that produces a new {@code Result} if this {@code Result} is
     *     failed
     * @return the {@code Result} produced by either {@code successMapper} or {@code failureMapper}
     * @throws NullPointerException if this {@code Result} is successful and {@code successMapper} is {@code null} or
     *     returns {@code null}; or if this {@code Result} is failed and {@code
     *     failureMapper} is {@code null} or returns {@code null}
     * @see #flatMapFailure(Function)
     * @see #flatMapSuccess(Function)
     */
    <S2, F2> Result<S2, F2> flatMap(
            Function<? super S, ? extends Result<? extends S2, ? extends F2>> successMapper,
            Function<? super F, ? extends Result<? extends S2, ? extends F2>> failureMapper);

    /**
     * Indicates whether some other object is "equal to" this {@code Result}.
     * <p>
     * The other object is considered equal if:
     * <ul>
     * <li>it is also a {@code Result} and;
     * <li>both objects are instances of the same class and;
     * <li>their values are "equal to" each other via {@code equals()}.
     * </ul>
     *
     * @param obj the object to be tested for equality
     * @return {@code true} if the other object is "equal to" this object; otherwise {@code false}
     * @see #hashCode()
     */
    @Override
    boolean equals(Object obj);

    /**
     * Returns the hash code of this {@code Result}'s value.
     *
     * @return hash code value of this {@code Result}'s value
     */
    @Override
    int hashCode();

    /**
     * Returns a string representation of this {@code Result}.
     * <p>
     * The exact presentation format is unspecified and may vary between implementations and versions.
     *
     * @implSpec The returned string should be suitable for debugging and must include the string representation of its
     *     value. Successful and failed {@code Result} instances must be unambiguously differentiable.
     * @return a string representation of this {@code Result}
     */
    @Override
    String toString();
}
