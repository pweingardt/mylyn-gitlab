/*******************************************************************************
 * Copyright (c) 2014, Paul Weingardt
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Paul Weingardt - initial API and implementation
 *******************************************************************************/



package de.weingardt.mylyn.gitlab.core;

import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.core.data.TaskMapper;

public class GitlabTaskMapper extends TaskMapper {

	public GitlabTaskMapper(TaskData taskData) {
		super(taskData);
	}

}
