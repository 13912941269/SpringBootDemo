package com.chemguan.util;

import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

@Component
public class YZM {

	// ��֤��ͼƬ�Ŀ�ȡ�
	private int width = 60;
	// ��֤��ͼƬ�ĸ߶ȡ�
	private int height = 20;
	// ��֤���ַ����
	private int codeCount = 4;
	private int x = 0;
	// ����߶�
	private int fontHeight;
	private int codeY;
	char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
			'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
			'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	/**
	 * ��ʼ����֤ͼƬ����
	 */
	public void initxuan() throws ServletException {
		// ��web.xml�л�ȡ��ʼ��Ϣ
		// ���
		String strWidth = "110";
		// �߶�
		String strHeight = "30";
		// �ַ����
		String strCodeCount = "4";
		// �����õ���Ϣת������ֵ
		try {
			if (strWidth != null && strWidth.length() != 0) {
				width = Integer.parseInt(strWidth);
			}
			if (strHeight != null && strHeight.length() != 0) {
				height = Integer.parseInt(strHeight);
			}
			if (strCodeCount != null && strCodeCount.length() != 0) {
				codeCount = Integer.parseInt(strCodeCount);
			}
		} catch (NumberFormatException e) {
		}
		x = width / (codeCount + 1);
		fontHeight = height - 2;
		codeY = height - 4;
	}

	public void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		initxuan();
		// ����ͼ��buffer
		BufferedImage buffImg = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = buffImg.createGraphics();
		// ����һ��������������
		Random random = new Random();
		// ��ͼ�����Ϊ��ɫ
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		// �������壬����Ĵ�СӦ�ø��ͼƬ�ĸ߶�������
		Font font = new Font("Fixedsys", Font.PLAIN, fontHeight);
		// �������塣
		g.setFont(font);
		// ���߿�
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, width - 1, height - 1);
		// ������160�������ߣ�ʹͼ���е���֤�벻�ױ��������̽�⵽��
		g.setColor(Color.BLACK);
		for (int i = 0; i < 10; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}
		// randomCode���ڱ������������֤�룬�Ա��û���¼�������֤��
		StringBuffer randomCode = new StringBuffer();
		int red = 0, green = 0, blue = 0;
		// ������codeCount���ֵ���֤�롣
		for (int i = 0; i < codeCount; i++) {
			// �õ����������֤�����֡�
			String strRand = String.valueOf(codeSequence[random.nextInt(36)]);
			// ����������ɫ������������ɫֵ�����������ÿλ���ֵ���ɫֵ������ͬ��
			red = random.nextInt(255);
			green = random.nextInt(255);
			blue = random.nextInt(255);
			// �����������ɫ����֤����Ƶ�ͼ���С�
			g.setColor(new Color(red, green, blue));
			g.drawString(strRand, (i + 1) * x, codeY);
			// ��������ĸ�����������һ��
			randomCode.append(strRand);
		}
		// ����λ���ֵ���֤�뱣�浽Session�С�
		HttpSession session = req.getSession();
		session.setAttribute("validateCode", randomCode.toString());
		// ��ֹͼ�񻺴档
		resp.setHeader("Pragma", "no-cache");
		resp.setHeader("Cache-Control", "no-cache");
		resp.setDateHeader("Expires", 0);
		resp.setContentType("image/jpeg");
		// ��ͼ�������Servlet������С�
		ServletOutputStream sos = resp.getOutputStream();
		ImageIO.write(buffImg, "jpeg", sos);
		sos.close();
	}

}
