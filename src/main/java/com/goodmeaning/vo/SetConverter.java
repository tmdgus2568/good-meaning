package com.goodmeaning.vo;

import java.util.EnumSet;
import java.util.Set;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class SetConverter implements AttributeConverter<EnumSet<Category>, String> {

	@Override
	public String convertToDatabaseColumn(EnumSet<Category> attribute) {
		StringBuilder sb = new StringBuilder();
        for (Category c : attribute) { 
            sb.append(c + ",");
        }
        return sb.toString();

	}

	@Override
	public EnumSet<Category> convertToEntityAttribute(String dbData) {
		// TODO Auto-generated method stub
		return null;
	}

}

