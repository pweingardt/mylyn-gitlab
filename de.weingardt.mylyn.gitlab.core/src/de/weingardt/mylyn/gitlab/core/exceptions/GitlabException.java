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



package de.weingardt.mylyn.gitlab.core.exceptions;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;

import de.weingardt.mylyn.gitlab.core.GitlabPluginCore;

public class GitlabException extends CoreException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8626757701151868815L;

	public GitlabException(String message) {
		super(new Status(Status.ERROR, GitlabPluginCore.ID_PLUGIN, message));
	}

}
