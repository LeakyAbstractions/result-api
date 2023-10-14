/**
 * A Java library to handle success and failure without exceptions
 * <p>
 * <img src="https://dev.leakyabstractions.com/result/result.svg" alt="Result Library">
 * <h2>Result Library for Java</h2>
 * <p>
 * The purpose of this library is to provide a type-safe encapsulation of operation results that may have succeeded or
 * failed, instead of throwing exceptions.
 * <p>
 * If you like {@link java.util.Optional} but feel that it sometimes falls too short, you'll love {@link Result}.
 * <p>
 * The best way to think of {@code Result} is as a super-powered version of {@code Optional}. The only difference is
 * that whereas {@code Optional} may contain a successful value or express the absence of a value, {@code Result}
 * contains either a successful value or a failure value that explains what went wrong.
 * <h3>Result Library in a Nutshell</h3>
 * <p>
 * Before {@code Result}, we would wrap exception-throwing {@code foobar} method invocation inside a {@code try} block
 * so that errors can be handled inside a {@code catch} block.
 *
 * <pre class="row-color rowColor">
 * <code>&nbsp;
 * public int getFoobarLength() {
 *     int length;
 *     try {
 *         final String result = foobar();
 *         this.commit(result);
 *         length = result.length();
 *     } catch (SomeException problem) {
 *         this.rollback(problem);
 *         length = -1;
 *     }
 *     return length;
 * }</code>
 * </pre>
 * <p>
 * This approach is lengthy, and that's not the only problem -- it's also slow. Conventional wisdom says that
 * exceptional logic shouldn't be used for normal program flow. {@code Result} makes us deal with expected,
 * non-exceptional error situations explicitly as a way of enforcing good programming practices.
 * <p>
 * Let's now look at how the above code could be refactored if method {@code foobar} returned a {@code Result} object
 * instead of throwing an exception:
 *
 * <pre class="row-color rowColor">
 * <code>&nbsp;
 * public int getFoobarLength() {
 *     final Result&lt;String, SomeFailure&gt; result = foobar();
 *     result.ifSuccessOrElse(this::commit, this::rollback);
 *     final Result&lt;Integer, SomeFailure&lt; resultLength = result.mapSuccess(String::length);
 *     return resultLength.orElse(-1);
 * }</code>
 * </pre>
 * <p>
 * In the above example, we use only four lines of code to replace the ten that worked in the first example. But we can
 * make it even shorter by chaining methods in typical functional programming style:
 *
 * <pre class="row-color rowColor">
 * <code>&nbsp;
 * public int getFoobarLength() {
 *     return foobar().ifSuccessOrElse(this::commit, this::rollback).mapSuccess(String::length).orElse(-1);
 * }</code>
 * </pre>
 * <p>
 * In fact, since we are using {@code -1} here just to signal that the underlying operation failed, we'd be better off
 * returning a {@code Result} object upstream:
 *
 * <pre class="row-color rowColor">
 * <code>&nbsp;
 * public Result&lt;Integer, SomeFailure&gt; getFoobarLength() {
 *     return foobar().ifSuccessOrElse(this::commit, this::rollback).mapSuccess(String::length);
 * }</code>
 * </pre>
 * <p>
 * This allows others to easily compose operations on top of ours, just like we did with foobar.
 *
 * @author Guillermo Calvo
 * @see Result
 * @see <a href="https://dev.leakyabstractions.com/result/">Project website</a>
 * @see <a href="https://github.com/LeakyAbstractions/result/">Source code</a>
 */

package com.leakyabstractions.result.api;
