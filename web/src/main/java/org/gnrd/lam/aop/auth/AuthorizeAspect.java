/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * Copyright 2023 gnrd
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

package org.gnrd.lam.aop.auth;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.gnrd.lam.common.exception.BaseException;
import org.gnrd.lam.common.exception.ECode;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AuthorizeAspect {

    private final SpelExpressionParser spelExpressionParser;

    private final BeanFactoryResolver beanFactoryResolver;

    public AuthorizeAspect(BeanFactory beanFactory) {
        this.spelExpressionParser = new SpelExpressionParser();
        this.beanFactoryResolver = new BeanFactoryResolver(beanFactory);
    }

    @Pointcut("@annotation(authorize)")
    public void point(Authorize authorize) {}

    @Before(value = "point(authorize)")
    public void successLog(JoinPoint joinPoint, Authorize authorize) {
        // 创建StandardEvaluationContext
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
        evaluationContext.setBeanResolver(beanFactoryResolver);
        String value = authorize.value();
        Expression expression = spelExpressionParser.parseExpression(value);
        Boolean result = expression.getValue(evaluationContext, Boolean.class);
        if (Boolean.FALSE.equals(result)) {
            throw new BaseException(HttpStatus.BAD_REQUEST, ECode.E_100002);
        }
    }
}
