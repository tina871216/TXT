import java.io.*;
import java.lang.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;

public class File_TxT
{
	public static void copyFile(File source, File dest) throws IOException
	{  
		FileChannel inputChannel = null;
		FileChannel outputChannel = null;
		try
		{
			inputChannel = new FileInputStream(source).getChannel();
			outputChannel = new FileOutputStream(dest).getChannel();
			outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
		}
		finally
		{
			inputChannel.close();
			outputChannel.close();
		}
	}
	
	public static void readfile(String FilePath)
	{
		System.out.println("----------\n");
		File file = new File(FilePath);
		
		if (file.isDirectory())
		{
			String[] filelist = file.list();
			for (int i = 0; i < filelist.length; i++)
			{
				File readfile = new File(filelist[i]);
				if ( ! readfile.isDirectory()) //不要其他資料，只要txt檔
				{
					String FileName = readfile.getName();
					int FileTXT = FileName.lastIndexOf(".txt");

					if (FileTXT != -1)
					{
						StringBuffer sb = new StringBuffer(FileName);
						StringBuffer NewFileName = sb.insert(FileTXT, "-複製");
						
						//複製TXT
						try
						{
							File source = new File(FilePath + FileName);
							File dest = new File(FilePath + "新/" + FileName);
							copyFile(source,dest);
							System.out.println(FileName + "已移至新資料夾\n");
						}
						catch (IOException ex)
						{
							ex.printStackTrace();
						}
						//修改TXT
						String str = null;
						try
						{
							FileInputStream fis = new FileInputStream(FilePath + "/新/" + FileName);
							InputStreamReader isr = new InputStreamReader(fis, getFileCharsetName(FileName));
							BufferedReader br = new BufferedReader(isr);
							
							char by[] = new char[fis.available()];
							br.read(by);
							str = new String(by);
							br.close();
						}
						catch(IOException ex)
						{
							System.out.println(ex.getMessage());
						}
						//System.out.println("原始內容：\n" + str);
						//System.out.println("----------\n");
						String newstr=str.replace("\n", "\n\n");
						//System.out.println("新內容：\n" + newstr);
						System.out.println("----------\n");
						try
						{
							FileWriter fw = new FileWriter(FilePath + "/新/" + FileName);
							fw.write(newstr);
							fw.close();
						}
						catch(IOException ex)
						{
							System.out.println(ex.getMessage());
						}/*
						File deleteFile = new File(FilePath + FileName);
						deleteFile.delete();*/
					}
				}
			}
		}
	}
	
	public static String getFileCharsetName(String fileName) throws IOException
	{
		BufferedInputStream bin;
		int bom = 0;
		String str = " ";
		String str2 = "";
		try
		{
			bin = new BufferedInputStream(new FileInputStream(fileName));
			bom = (bin.read() << 8) + bin.read();
			byte bs[] = new byte[10];
			while(str.matches("\\s+\\w*"))
			{
				bin.read(bs);
				str = new String(bs, "UTF-8");
			}
			str2 = new String(bs, "GBK");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String code = null;
		switch (bom) {
		case 0xEFBB:
			code = "UTF-8";
			break;
		case 0xFEFF:
			code = "Unicode";
			break;
		case 0xFFFE:
			code = "UTF-16";
			break;
		default:
			if (str.length() <=str2.length()) {
				code = "UTF-8";
			} else {
				code = "GBK";
			}
		}
		return code;
    }
	
	public static void main(String[] args)
	{
		readfile("./");
	}
}