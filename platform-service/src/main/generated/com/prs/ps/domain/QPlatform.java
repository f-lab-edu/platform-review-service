package com.prs.ps.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPlatform is a Querydsl query type for Platform
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPlatform extends EntityPathBase<Platform> {

    private static final long serialVersionUID = -1995421188L;

    public static final QPlatform platform = new QPlatform("platform");

    public final QAuditable _super = new QAuditable(this);

    public final NumberPath<Integer> avgScore = createNumber("avgScore", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDt = _super.createdDt;

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDt = _super.modifiedDt;

    public final StringPath name = createString("name");

    public final NumberPath<Long> reviewCount = createNumber("reviewCount", Long.class);

    public final EnumPath<com.prs.ps.type.PlatformStatus> status = createEnum("status", com.prs.ps.type.PlatformStatus.class);

    public final NumberPath<Long> totalScore = createNumber("totalScore", Long.class);

    public final StringPath url = createString("url");

    public final NumberPath<Long> version = createNumber("version", Long.class);

    public QPlatform(String variable) {
        super(Platform.class, forVariable(variable));
    }

    public QPlatform(Path<? extends Platform> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPlatform(PathMetadata metadata) {
        super(Platform.class, metadata);
    }

}

