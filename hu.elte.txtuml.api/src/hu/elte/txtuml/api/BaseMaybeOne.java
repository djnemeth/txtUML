package hu.elte.txtuml.api;

import hu.elte.txtuml.api.backend.problems.MultiplicityException;
import hu.elte.txtuml.api.blocks.ParameterizedCondition;

import java.util.Iterator;

/**
 * Base class of association ends having a 0..1 multiplicity.
 * <p>
 * See the documentation of the {@link hu.elte.txtuml.api} package to get an
 * overview on modeling in txtUML.

 * @author G�bor Ferenc Kov�cs
 *
 * @param <T>
 *            the type of model objects to be contained in this collection
 */
class BaseMaybeOne<T extends ModelClass> extends AssociationEnd<T> {

	/**
	 * The model object contained in this collection. If <code>null</code>, this
	 * collection is empty.
	 */
	private T obj = null;

	/**
	 * Creates an empty, unfinalized <code>BaseMaybeOne</code> instance which
	 * might be changed once using the <code>init</code> method.
	 */
	public BaseMaybeOne() {
		isFinal = false;
	}

	/**
	 * An initilazing method which changes this instance to be a copy of the
	 * <code>other</code> collection, if certain conditions are met:
	 * <ul>
	 * <li>this instance is unfinalized, so the value of its
	 * <code>isFinal</code> field is <code>false</code>,
	 * <li>the given collection is a subclass of BaseMaybeOne</li>
	 * </ul>
	 * After this method returns (either way), this association end is surely
	 * finalized, so its <code>isFinal</code> field is set to be
	 * <code>true</code>.
	 * 
	 * @param other
	 *            the other collection to copy
	 * @return this instance
	 * @throws NullPointerException
	 *             if <code>other</code> is <code>null</code>
	 */
	@Override
	final AssociationEnd<T> init(Collection<T> other) {
		if (!isFinal && other != null && other instanceof BaseMaybeOne) {
			this.obj = ((BaseMaybeOne<T>) other).obj;
		}
		isFinal = true;
		return this;
	}

	/**
	 * Creates a finalized <code>BaseMaybeOne</code> instance to contain the
	 * specified value.
	 * <p>
	 * Finalized means that this object will operate as its class was immutable.
	 * 
	 * @param object
	 *            the model object to contain
	 */
	BaseMaybeOne(T object) {
		this.obj = object;
	}

	@Override
	public final Iterator<T> iterator() {
		return new Iterator<T>() {

			private boolean hasNext = true;

			@Override
			public T next() {
				hasNext = false;
				return obj;
			}

			@Override
			public boolean hasNext() {
				return hasNext;
			}

			@Override
			public void remove() {
			}

		};
	}

	@Override
	public final ModelInt count() {
		return this.obj != null ? ModelInt.ONE : ModelInt.ZERO;
	}

	@Override
	public final ModelBool contains(ModelClass object) {
		return new ModelBool(this.obj == null ? object == null
				: this.obj.equals(object));
	}

	@Override
	public final T selectOne() {
		return obj;
	}

	@Override
	public final T selectOne(ParameterizedCondition<T> cond) {
		if (obj == null || cond.check(obj).getValue()) {
			return obj;
		}
		return null;
	}

	@Override
	public final Collection<T> selectAll(ParameterizedCondition<T> cond) {
		return new BaseMaybeOne<T>(selectOne(cond));
	}

	@Override
	public final Collection<T> add(T object) {
		if (obj == null) {
			return new BaseMaybeOne<T>(object);
		}
		return new BaseMany<T>(obj, object);
	}

	@Override
	public final Collection<T> addAll(Collection<T> objects) {
		return new BaseMany<T>(objects, this.obj);
	}

	@Override
	public final Collection<T> remove(T object) {
		if (object == null || object.equals(this.obj)) {
			return this;
		}
		return new Empty<T>();
	}

	@Override
	@SuppressWarnings("unchecked")
	final <S extends AssociationEnd<T>> S typeKeepingAdd(T object)
			throws MultiplicityException {
		if (object == null) {
			return (S) this;
		} else if (this.obj != null && !this.obj.equals(object)) {
			throw new MultiplicityException(); // TODO set message
		}
		return (S) new BaseMaybeOne<T>(object);
	}

	@Override
	@SuppressWarnings("unchecked")
	final <S extends AssociationEnd<T>> S typeKeepingRemove(T object) {
		if (object == null || !object.equals(this.obj)) {
			return (S) this;
		}
		return (S) new BaseMaybeOne<T>();
	}

	@Override
	public String toString() {
		return obj == null ? "null" : obj.toString();
	}

	@Override
	boolean checkUpperBound() {
		return false;
	}

	@Override
	boolean checkLowerBound() {
		return true; // There is no lower bound of MaybeOne.
	}
}
