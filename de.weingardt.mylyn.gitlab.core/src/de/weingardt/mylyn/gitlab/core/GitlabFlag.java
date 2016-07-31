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

/**
 * A flag for Gitlab attributes.
 * @author paul
 *
 */
public enum GitlabFlag {
	/**
	 * Sets the attribute to ReadOnly.
	 */
	READ_ONLY,

	/**
	 * Default Attribute
	 */
	ATTRIBUTE,

	/**
	 * A flag to indicate, that an attribute describes a person.
	 */
	PEOPLE;
}
