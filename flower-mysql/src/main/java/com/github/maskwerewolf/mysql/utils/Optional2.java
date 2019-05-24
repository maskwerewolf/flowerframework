package com.github.maskwerewolf.mysql.utils;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Created with IntelliJ IDEA.
 * created by  chenhongbo[maskwerewolf@163.com]
 * Date: 2019/5/10
 */
public final class Optional2<T> {

    private static final Optional2<?> EMPTY = new Optional2<>();


    private final T value;


    private Optional2() {
        this.value = null;
    }


    public static <T> Optional2<T> empty() {
        @SuppressWarnings("unchecked")
        Optional2<T> t = (Optional2<T>) EMPTY;
        return t;
    }


    private Optional2(T value) {
        this.value = Objects.requireNonNull(value);
    }


    public static <T> Optional2<T> of(T value) {
        return new Optional2<>(value);
    }


    public static <T> Optional2<T> ofNullable(T value) {
        return value == null ? empty() : of(value);
    }


    public T get() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }


    public boolean isPresent() {
        return value != null;
    }


    public void ifPresent(Consumer<? super T> consumer) {
        if (value != null) {
            consumer.accept(value);
        }
    }


    public Optional2<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        if (!isPresent()) {
            return this;
        } else {
            return predicate.test(value) ? this : empty();
        }

    }


    public <U> Optional2<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        if (!isPresent()) {
            return empty();
        } else {
            return Optional2.ofNullable(mapper.apply(value));
        }
    }


    public <U> Optional2<U> flatMap(Function<? super T, Optional2<U>> mapper) {
        Objects.requireNonNull(mapper);
        if (!isPresent()) {
            return empty();
        } else {
            return Objects.requireNonNull(mapper.apply(value));
        }
    }


    public T orElse(T other) {
        return value != null ? value : other;
    }


    public T orElseGet(Supplier<? extends T> other) {
        return value != null ? value : other.get();
    }


    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (value != null) {
            return value;
        } else {
            throw exceptionSupplier.get();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Optional2)) {
            return false;
        }

        Optional2<?> other = (Optional2<?>) obj;
        return Objects.equals(value, other.value);
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }


    //========================================================

    public boolean isPresentFalse() {
        if (value instanceof Boolean) {
            return !((Boolean) value);
        }
        return true;
    }

    public boolean isPresentTrue() {
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return false;
    }


    public void isPresentTrue(Consumer<? super T> consumer) {
        if (value instanceof Boolean) {
            if ((Boolean) value) {
                consumer.accept(value);
            }
        }
    }

    public void isPresentFalse(Consumer<? super T> consumer) {
        if (value instanceof Boolean) {
            if (!(Boolean) value) {
                consumer.accept(value);
            }
        }
    }

    public void ifPresentTrue(Predicate<? super T> predicate, Consumer<? super T> consumer) {
        Objects.requireNonNull(predicate);
        if (isPresent() && predicate.test(value)) {
            consumer.accept(value);
        }
    }


    public void ifPresentFalse(Predicate<? super T> predicate, Consumer<? super T> consumer) {
        Objects.requireNonNull(predicate);
        if (isPresent() && !predicate.test(value)) {
            consumer.accept(value);
        }
    }


    public <X extends Throwable> T ifPresentFalseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (value instanceof Boolean && (Boolean) value) {
            return value;
        } else {
            throw exceptionSupplier.get();
        }
    }

    public <X extends Throwable> T ifPresentTrueThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (value instanceof Boolean && !(Boolean) value) {
            return value;
        } else {
            throw exceptionSupplier.get();
        }
    }

    public <X extends Throwable> void ifPresentTrueThrow(Predicate<? super T> predicate, Supplier<? extends X> exceptionSupplier) throws X {
        Objects.requireNonNull(predicate);
        if (isPresent() && predicate.test(value)) {
            throw exceptionSupplier.get();
        }
    }

    public <X extends Throwable> void ifPresentFalseThrow(Predicate<? super T> predicate, Supplier<? extends X> exceptionSupplier) throws X {
        Objects.requireNonNull(predicate);
        if (isPresent() && !predicate.test(value)) {
            throw exceptionSupplier.get();
        }
    }

    @Override
    public String toString() {
        return value != null
                ? String.format("Optional2[%s]", value)
                : "Optional2.empty";
    }
}
