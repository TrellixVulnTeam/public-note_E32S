逻辑地址空间看计算机的bit数，物理地址空间看内存条。
## 页表
### 页表概述
每个运行程序都有个页表，属于程序的一种运行状态，会动态变化。PTBR页表基地址寄存器，
* 标识位
    * dirty bit
    * resident bit，逻辑页是否存在于物理帧中的标识。
    * clock bit
* 帧号f

* 问题
    * 页表空间, 页表项可能会非常大
        * 如64bit的计算机，每个页大小1k字节，则页表会大至2的54次方(因为逻辑地址空间是64bit)。
        * 每个程序一个页表，N个程序就有N个，占用大量的空间。
    * 空间大后，导致的性能问题
        * 页表很大，无法存放到CPU，只能放到内存，导致多次访问内存。实际上页表就是放在内存中的。

通过缓存和多级分表来解决。

### 转换后备缓冲区TLB
TLB就是用缓存来解决页表的性能问题的。TLB位于CPU内部，将会缓存近期访问的页帧转化表项。
* TLB使用关联内存实现（我估计就是一种hash）
* TLB命中，物理帧号可以很快被获取。
* TLB未命中，对应的表项更新到TLB。

x86可以自动在miss后，将表项通过硬件同步到TLB，有些CPU不支持，需要操作系统来进行软件支持。
### 二级/多级页表
多级页表用来减小页表过大的问题。页表过大的问题通常是逻辑地址空间过大，但是物理地址空间不大导致的，页表的建立是根据逻辑地址空间而来的。但是若逻辑地址空间大，物理地址空间小，很多页的都是没有数据的，没有必要将这些页表项记录到内存当中。二级页表会将页数进行划分，生成一级页表项目和偏移，者用于定位到二级页表。在这里，二级页表就会有很多不会加载到内存中去，因为很多二级页表不会有和帧的映射关系。
### 反向页表
主要还是用于解决逻辑地址空间过大，导致的页表过大的问题。

# 虚拟内存

## 起因

## 覆盖技术

## 交换技术

## 虚存技术
覆盖技术，程序员的开发成本较大。交换技术，粒度较大。
虚拟内存大小=内存大小+swap
虚存技术需要程序具备局部性。即在一定的时间内，所执行的指令地址和访问的数据地址都集中在一定的区域。
* 时间局部性，当前指令和数据集中在一定的时间内访问。
* 空间局部性，当前指令和临近需要执行指令，当前的数据和临近需要访问的数据，都集中存储。

页表数据:
* 驻留位，1---逻辑页存在于物理内存，0---逻辑页不存在于物理内存
* 保护位，设置页的权限，只读、可读可执行等权限
* 修改位，表明此页是否在内存中被修改过，若没有被修改过，则不用重新写到外存，外存中的数据和内存中的本就一致，若被修改过，则需要回写到外存。
* 访问位，若改页面被访问过则为1，若没有被访问过则为0


# 进程
## 进程描述
进程是一个具有一定独立功能的程序在一个数据集合上的动态运行。
进程的组成：
* 程序的代码
* 程序处理的数据
* 程序计数器中的值，指示下一条运行的指令
* 一组通用寄存器的值
* 一组系统资源（如文件）

进程控制块(PCB), 操作系统管理和控制进程运行所用的信息集合:
* 进程标识信息
* 进程状态信息
* 进程控制信息
## 进程状态

## 线程
* 进程是资源分配的单位，用于管理和组织资源，同一进程下的线程共享这些 资源
* 线程是CPU的调度单位，除了一些必要的独享资源(stack/寄存器)外的所有资源都是共享的
### 线程的实现
#### 1).用户线程
对操作系统而言不可知的线程，由用户态的相关库进行管理。用户线程和内核线程的对应关系：
* 多对一
* 一对一
* 多对多

线程控制块由库实现。有以下特点:
* 常用于不支持线程的多进程系统。
* 进程被阻塞，会导致所有的线程全部阻塞(主要是多对一模型)。
* 线程切换由线程库完成，无需切换用户态和内核态。
* 允许每个应用程序可以定制线程调度算法。
* 线程会把一个进程的的时间片进一步细分，导致每个线程的执行时间更短。
#### 2).内核线程
操作系统管理的线程，特点：
* 内核维护进程和线程的上下文
* 线程的创建、切换和终止 需要进行内核态与用户态的切换，开销较大。
* 线程切换由内核完成，不会影响进程中的其他线程。

windows 2000/NT/XP 通常用的是该方案
#### 3).轻量级进程
Solaris和Linux常用的实现机制。一个进程可以有一个或多个`轻量级进程`。

## 进程的上下文切换
上下文切换指的是从运行态切换到其他状态，并且调度其他进程。
* 必须在切换之前需要存储许多部分的进程上下文

## 进程通信

## 进程互斥与同步

## 死锁 