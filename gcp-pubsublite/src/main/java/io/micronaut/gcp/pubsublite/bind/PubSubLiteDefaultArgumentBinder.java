/*
 * Copyright 2017-2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.gcp.pubsublite.bind;

import io.micronaut.core.convert.ArgumentConversionContext;

import javax.inject.Singleton;

/**
 * Default body binder of PubSub consumers. If no @{@link io.micronaut.messaging.annotation.Body} arguments are annotated.
 * Delegates to {@link PubSubLiteBodyBinder}
 *
 * Based on {@link io.micronaut.gcp.pubsub.bind.PubSubDefaultArgumentBinder}
 *
 * @author Jacob Mims
 * @since 2.2.0
 */
@Singleton
public class PubSubLiteDefaultArgumentBinder implements PubSubLiteArgumentBinder<Object> {

    private final PubSubLiteBodyBinder bodyBinder;

    public PubSubLiteDefaultArgumentBinder(PubSubLiteBodyBinder defaultBinder) {
        this.bodyBinder = defaultBinder;
    }

    @Override
    public BindingResult<Object> bind(ArgumentConversionContext<Object> context, PubSubLiteConsumerState source) {
        return bodyBinder.bind(context, source);
    }
}