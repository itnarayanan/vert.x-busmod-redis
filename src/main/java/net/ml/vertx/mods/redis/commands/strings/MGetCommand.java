/*
 * Copyright 2011-2012 the original author or authors.
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
package net.ml.vertx.mods.redis.commands.strings;

import java.util.List;
import java.util.concurrent.Future;

import net.ml.vertx.mods.redis.CommandContext;
import net.ml.vertx.mods.redis.commands.Command;
import net.ml.vertx.mods.redis.commands.CommandException;

import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

/**
 * GetCommand
 * <p>
 * 
 * @author <a href="http://marx-labs.de">Thorsten Marx</a>
 */
public class MGetCommand extends Command {

	public static final String COMMAND = "mget";

	public MGetCommand() {
		super(COMMAND);
	}

	@Override
	public void handle(final Message<JsonObject> message, CommandContext context) throws CommandException {
		JsonArray keys = message.body.getArray("keys");
		
		checkNull(keys, "keys can not be null");
		
		try {
			
			final Future<List<String>> response = context.getConnection().mget(getStringArray(keys));
			
			List<String> values = response.get();
			JsonArray result;
			if (values != null && !values.isEmpty()) {
				result = new JsonArray(values.toArray());
			} else {
				result = new JsonArray();
			}
			
			response(message, result);
		} catch (Exception e) {
			sendError(message, e.getLocalizedMessage());
		}

	}
}
