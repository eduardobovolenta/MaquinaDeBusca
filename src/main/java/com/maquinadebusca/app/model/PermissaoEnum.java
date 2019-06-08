package com.maquinadebusca.app.model;

public class PermissaoEnum {

	public enum Permissao {
		ADMIN("A"), USUARIO("U");

		private final String code;

		public String getCode() {
			return this.code;
		}

		Permissao(final String code) {
			this.code = code;
		}
	}
}
