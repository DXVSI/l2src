package org.mmocore.gameserver.data.xml.parser;

import org.mmocore.commons.data.xml.AbstractDirParser;
import org.mmocore.gameserver.Config;
import org.mmocore.gameserver.data.xml.holder.ClassDataHolder;
import org.mmocore.gameserver.templates.player.ClassData;
import org.dom4j.Element;

import java.io.File;
import java.util.Iterator;

public final class ClassDataParser extends AbstractDirParser<ClassDataHolder>
{
	private static final ClassDataParser _instance = new ClassDataParser();

	public static ClassDataParser getInstance()
	{
		return _instance;
	}

	private ClassDataParser()
	{
		super(ClassDataHolder.getInstance());
	}

	@Override
	public File getXMLDir()
	{
		return new File(Config.DATAPACK_ROOT, "data/pc_parameters/class_data/");
	}

	@Override
	public boolean isIgnored(File f)
	{
		return false;
	}

	@Override
	public String getDTDFileName()
	{
		return "class_data.dtd";
	}

	@Override
	protected void readData(Element rootElement) throws Exception
	{
		for (Iterator<Element> iterator = rootElement.elementIterator(); iterator.hasNext();)
		{
			Element element = iterator.next();

			int classId = Integer.parseInt(element.attributeValue("class_id"));
			ClassData template = new ClassData(classId);
			for (Iterator<Element> subIterator = element.elementIterator(); subIterator.hasNext();)
			{
				Element subElement = subIterator.next();
				if ("lvl_up_data".equalsIgnoreCase(subElement.getName()))
				{
					for (Element e : subElement.elements())
					{
						int lvl = Integer.parseInt(e.attributeValue("lvl"));
						double hp = Double.parseDouble(e.attributeValue("hp"));
						double mp = Double.parseDouble(e.attributeValue("mp"));
						double cp = Double.parseDouble(e.attributeValue("cp"));
						template.addLvlUpData(lvl, hp, mp, cp);
					}
				}
			}
			getHolder().addClassData(template);
		}
	}
}
