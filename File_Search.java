import java.io.*;
import java.lang.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.CharBuffer;

public class File_Search
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
						//Show出所有TXT
						System.out.print(FileName);
						//複製TXT
						try
						{
							File source = new File(FilePath + FileName);
							File dest = new File(FilePath + "舊/" + NewFileName);
							copyFile(source,dest);
							System.out.println("已移至舊資料夾");
						}
						catch (IOException ex)
						{
							ex.printStackTrace();
						}
						//閱讀TXT
						try
						{
							FileInputStream fis = new FileInputStream(FilePath + "/" + FileName);
							InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
							BufferedReader br = new BufferedReader(isr);
							
							CharBuffer target = CharBuffer.allocate(FileName.length());
							while (br.ready())
							{
								System.out.println(br.readLine(target));
							}
							br.close();
						}
						catch (FileNotFoundException ex)
						{
							System.out.println(ex.getMessage());
						}
						catch (IOException ex)
						{
							ex.printStackTrace();
						}
						//新增TXT
						BufferedWriter bw = null;
						try
						{
							FileOutputStream fos = new FileOutputStream(FilePath + "/新/" + FileName);
							OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
							bw = new BufferedWriter(osw);

							bw.flush();
						}
						catch (Exception ex)
						{
							ex.printStackTrace();
						}
						finally
						{
							if (bw != null)
							{
								try
								{
									bw.close();
								}
								catch (IOException ex)
								{
									ex.printStackTrace();
								}
							}
						}
					}
				}
			}
		}
		System.out.println("------------");
		System.out.println("導入完成！");
	}
	public static void main(String[] args)
	{
		readfile("./");
	}
}