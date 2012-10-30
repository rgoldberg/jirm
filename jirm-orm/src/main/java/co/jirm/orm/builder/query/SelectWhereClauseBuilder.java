package co.jirm.orm.builder.query;

import java.util.List;

import co.jirm.orm.builder.AbstractWhereClauseBuilder;
import co.jirm.orm.builder.ImmutableCondition;

import com.google.common.collect.Lists;


public class SelectWhereClauseBuilder<I> extends AbstractWhereClauseBuilder<SelectWhereClauseBuilder<I>, I> implements SelectClause<I> {
	
	private final SelectClause<I> parent;
	private final List<SelectClause<I>> children = Lists.newArrayList();
	
	private SelectWhereClauseBuilder(SelectClause<I> parent, ImmutableCondition condition) {
		super(condition);
		this.parent = parent;
	}
	
	static <I> SelectWhereClauseBuilder<I> newWhereClauseBuilder(SelectClause<I> parent, ImmutableCondition condition) {
		return new SelectWhereClauseBuilder<I>(parent, condition);
	}
	
	public OrderByClauseBuilder<I> orderBy(String sql) {
		return addClause(OrderByClauseBuilder.newInstance(this, sql));
	}
	public LimitClauseBuilder<I> limit(String sql) {
		return addClause(LimitClauseBuilder.newInstance(this, sql));
	}
	public LimitClauseBuilder<I> limit(Number i) {
		return addClause(LimitClauseBuilder.newInstanceWithLimit(this, i));
	}
	public OffsetClauseBuilder<I> offset(String sql) {
		return addClause(OffsetClauseBuilder.newInstance(this, sql));
	}
	public OffsetClauseBuilder<I> offset(Number i) {
		return addClause(OffsetClauseBuilder.newInstanceWithOffset(this, i));
	}

	@Override
	protected SelectWhereClauseBuilder<I> getSelf() {
		return this;
	}
	
	@Override
	public <C extends SelectClauseVisitor> C accept(C visitor) {
		visitor.visit(getSelf());
		for (SelectClause<I> k : children) {
			k.accept(visitor);
		}
		return visitor;
	}
	
	protected <K extends SelectClause<I>> K addClause(K k) {
		children.add(k);
		return k;
	}

	@Override
	public SelectClauseType getType() {
		return SelectClauseType.WHERE;
	}

	@Override
	public I query() {
		return parent.query();
	}

}