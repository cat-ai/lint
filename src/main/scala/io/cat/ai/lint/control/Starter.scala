package io.cat.ai.lint.control

trait Starter[-A, +B] {

  def start(a: A): B
}