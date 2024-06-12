package com.prs.ps.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAuditable is a Querydsl query type for Auditable
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QAuditable extends EntityPathBase<Auditable> {

    private static final long serialVersionUID = 479009612L;

    public static final QAuditable auditable = new QAuditable("auditable");

    public final DateTimePath<java.time.LocalDateTime> createdDt = createDateTime("createdDt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> modifiedDt = createDateTime("modifiedDt", java.time.LocalDateTime.class);

    public QAuditable(String variable) {
        super(Auditable.class, forVariable(variable));
    }

    public QAuditable(Path<? extends Auditable> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAuditable(PathMetadata metadata) {
        super(Auditable.class, metadata);
    }

}

