/*******************************************************************************
 * Copyright 2005, 2006, 2007, 2008 Acessibilidade Brasil
 * Este arquivo é parte do programa ASES - Avaliador e Simulador para AcessibilidadE de Sítios
 * O ASES é um software livre; você pode redistribui-lo e/ou modifica-lo dentro dos termos da Licença Pública Geral GNU como
 * publicada pela Fundação do Software Livre (FSF); na versão 2 da Licença, ou (na sua opnião) qualquer versão posterior.
 * Este programa é distribuido na esperança que possa ser  util, mas SEM NENHUMA GARANTIA; sem uma garantia implicita de ADEQUAÇÂO a qualquer  MERCADO ou APLICAÇÃO EM PARTICULAR. Veja a Licença Pública Geral GNU para maiores detalhes.
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU, sob o título "LICENCA.txt", junto com este programa, se não, escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *******************************************************************************/
/*******************************************************************************
 * Copyright (c) 2005, 2006, 2007 Acessibilidade Brasil.
 * 
 * This file is part of ASES.
 *
 * ASES is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * A copy of the license can be found at 
 * http://www.gnu.org/copyleft/lesser.txt.
 *******************************************************************************/
package br.org.acessobrasil.silvinha2.bv.filtros;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
 
public class SomeImageOptions {
    private JScrollPane getContent(BufferedImage[] images) {
        JPanel panel = new JPanel(new GridLayout(1,0));
        panel.add(wrap(copyToType(images[0], BufferedImage.TYPE_BYTE_GRAY)));
        panel.add(wrap(getComposite(images, 0.4f)));
        panel.add(wrap(clippedCopy(images)));
        return new JScrollPane(panel);
    }
 
    private BufferedImage copyToType(BufferedImage src, int type) {
        int w = src.getWidth();
        int h = src.getHeight();
        BufferedImage copy = new BufferedImage(w, h, type);
        Graphics2D g2 = copy.createGraphics();
        g2.drawImage(src, 0, 0, null);
        g2.dispose();
        return copy;
    }
 
    private BufferedImage getComposite(BufferedImage[] images, float alpha) {
        int w = Math.max(images[0].getWidth(), images[1].getWidth());
        int h = Math.max(images[0].getHeight(), images[1].getHeight());
        int type = BufferedImage.TYPE_INT_RGB;   // good performance
        BufferedImage dest = new BufferedImage(w, h, type);
        Graphics2D g2 = dest.createGraphics();
        int x = (w - images[0].getWidth())/2;
        int y = (h - images[0].getHeight())/2;
        g2.drawImage(images[0], x, y, null);
        AlphaComposite ac = AlphaComposite.getInstance(
                                 AlphaComposite.SRC_OVER, alpha);
        g2.setComposite(ac);
        x = (w - images[1].getWidth())/2;
        y = (h - images[1].getHeight())/2;
        g2.drawImage(images[1], x, y, null);
        g2.dispose();
        return dest;
    }
 
    private BufferedImage clippedCopy(BufferedImage[] images) {
        int w = images[0].getWidth();
        int h = images[0].getHeight();
        int type = BufferedImage.TYPE_INT_RGB;
        BufferedImage dest = new BufferedImage(w, h, type);
        Graphics2D g2 = dest.createGraphics();
        g2.drawImage(images[0], 0, 0, null);
        Rectangle clip = new Rectangle(50, 50);
        // Put the clip where you want the image to appear.
        // Here, center it in base image.
        int x = (w - clip.width)/2;
        int y = (h - clip.height)/2;
        clip.setLocation(x, y);
        g2.setClip(clip);
        // Locate the image relative to the clip origin
        // to expose the part you want to see.
        // Here, show it at 20, 20 from its origin.
        x = x - 20;
        y = y - 20;
        g2.drawImage(images[1], x, y, null);
        g2.dispose();
        return dest;
    }
 
    private JLabel wrap(BufferedImage image) {
        ImageIcon icon = new ImageIcon(image);
        return new JLabel(icon, JLabel.CENTER);
    }
 
    private static void confirmClip(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        int type = BufferedImage.TYPE_INT_RGB;
        BufferedImage dest = new BufferedImage(w, h, type);
        Graphics2D g2 = dest.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.setPaint(Color.red);
        // Draw clip on image.
        g2.drawRect(20,20,50,50);
        g2.dispose();
        JOptionPane.showMessageDialog(null, new ImageIcon(dest), "", -1);
    }
 
    public static void main(String[] args) throws IOException {
        String[] ids = { "cougar.jpg", "Bird.gif" };
        BufferedImage[] images = new BufferedImage[ids.length];
        for(int j = 0; j < images.length; j++)
            images[j] = ImageIO.read(new File("images/" + ids[j]));
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setContentPane(new SomeImageOptions().getContent(images));
        f.setSize(400,400);
        f.setLocation(200,200);
        f.setVisible(true);
        confirmClip(images[1]);
    }
}

